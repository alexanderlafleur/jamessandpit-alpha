// ID3v2.java
//
// $Id: ID3v2.java,v 1.1 2003/07/05 18:43:36 axelwernicke Exp $
//
// de.vdheide.mp3: Access MP3 properties, ID3 and ID3v2 tags
// Copyright (C) 1999 Jens Vonderheide <jens@vdheide.de>
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Library General Public
// License as published by the Free Software Foundation; either
// version 2 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Library General Public License for more details.
//
// You should have received a copy of the GNU Library General Public
// License along with this library; if not, write to the
// Free Software Foundation, Inc., 59 Temple Place - Suite 330,
// Boston, MA  02111-1307, USA.

/**
 * Instances of this class contains an ID3v2 tag
 * <p>
 * Notes:
 * <p>
 * 1) There are two ways of detecting the size of padding used:
 *    a) The "Size of padding" field in the extended header
 *    b) Detecting all frames and substracting the tag's actual
 *       length from its' length in the header.
 *    Method a) is used in preference, so if a wrong padding
 *    size is stated in the extended header, all bad things
 *    may happen.
 * <p>
 * 2) Although the ID3v2 informal standard does not state it,
 *    this class will only detect an ID3v2 tag if is starts at
 *    the first byte of a file.
 * <p>
 * 3) There is no direct access to the header and extended header.
 *    Both are read, created and written internally.
 */

package de.vdheide.mp3;

import java.util.Vector;
import java.util.Enumeration;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class ID3v2
{
	
	/********** Constructors **********/
	
	/**
	 * Provides access to ID3v2 tag. When used with an InputStream, no writes are possible
	 * (<code>update</code> will fail with an <code>IOException</code>, so make sure you
	 * just read.
	 *
	 * @param in Input stream to read from. Stream position must be set to beginning of file
	 *        (i.e. position of ID3v2 tag).
	 * @exception IOException If I/O errors occur
	 * @exception ID3v2IllegalVersionException If file contains an IDv2 tag of higher version than
	 *            <code>VERSION</code>.<code>REVISION</code>
	 * @exception ID3v2WrongCRCException If file contains CRC and this differs from CRC calculated
	 *            from the frames
	 * @exception ID3v2DecompressionException If a decompression error occured while decompressing
	 *            a compressed frame
	 */
	public ID3v2(InputStream in) throws IOException, ID3v2IllegalVersionException, ID3v2WrongCRCException,
	ID3v2DecompressionException
	{
		this.file = null;
		
		// open file and read tag (if present)
		try
		{
			readHeader(in);
		}
		catch (NoID3v2HeaderException e)
		{
			// no tag
			header = null;
			extended_header = null;
			frames = null;
			
			// begin fix by axel.wernicke@gmx.de 03/02/01
			// clean up input stream ...
			in.close();
			in = null;
			// end fix

			return;
		}
		
		// tag present
		if (header.hasExtendedHeader())
		{
			readExtendedHeader(in);
		}
		else
		{
			extended_header = null;
		}
		
		readFrames(in);
		
		in.close();
		// begin fix by axel.wernicke@gmx.de 03/02/02
		in = null;
		// end fix
		
		is_changed = false;
	}
	
	
	/**
	 * Provides access to <code>file</code>'s ID3v2 tag
	 *
	 * @param file File to access
	 * @exception IOException If I/O errors occur
	 * @exception ID3v2IllegalVersionException If file contains an IDv2 tag of higher version than
	 *            <code>VERSION</code>.<code>REVISION</code>
	 * @exception ID3v2WrongCRCException If file contains CRC and this differs from CRC calculated
	 *            from the frames
	 * @exception ID3v2DecompressionException If a decompression error occured while decompressing
	 *            a compressed frame
	 */
	public ID3v2(File file) throws IOException, ID3v2IllegalVersionException, ID3v2WrongCRCException,
	ID3v2DecompressionException
	{
		this(new FileInputStream(file));
		this.file = file;
	}
	
	
	/********** Public variables **********/
	
	/**
	 * ID3v2 version
	 */
	public final static byte VERSION = 3;
	
	/**
	 * ID3v2 revision
	 */
	public final static byte REVISION = 0;
	
	
	/********** Public methods **********/
	
	/**
	 * This method undoes the effect of the unsynchronization scheme
	 * by replacing $FF $00 by $FF
	 *
	 * @param in Array of bytes to be "synchronized"
	 * @return Changed array or null if no "synchronization" was necessary
	 */
	public static byte []synchronize(byte []in)
	{
		boolean did_synch = false;
		byte out[] = new byte[in.length];
		int outpos = 0; // next position to write to
		
		for (int i=0; i<in.length; i++)
		{
			// Check every byte if it is $FF
			if (in[i] == (byte)255)
			{
				// synchronize if next byte is $00
				if (in[i+1] == 0)
				{
					did_synch = true;
					out[outpos++]=(byte)255;
					i++;
				}
				else
				{
					out[outpos++]=(byte)255;
				}
			}
			else
			{
				out[outpos++]=in[i];
			}
		}
		
		// make out smaller if necessary
		if (outpos!=in.length)
		{
			// removed one or more bytes
			byte []tmp = new byte[outpos];
			System.arraycopy(out, 0, tmp, 0, outpos);
			out = tmp;
		}
		
		if (did_synch == true)
		{
			return out;
		}
		else
		{
			return null;
		}
	}
	
	
	/**
	 * Unsynchronizes an array of bytes by replacing $FF 00 with
	 * $FF 00 00 and %11111111 111xxxxx with
	 * %11111111 00000000 111xxxxx.
	 *
	 * @param in Array of bytes to be "unsynchronized"
	 * @return Changed array or null if no change was necessary
	 */
	public static byte []unsynchronize(byte []in)
	{
		byte []out = new byte[in.length];
		int outpos = 0; // next position to write to
		boolean did_unsync = false;
		
		for (int i=0; i<in.length; i++)
		{
			// Check every byte in in if it is $FF
			if (in[i]==-1)
			{
				// yes, perhaps we must unsynchronize
				// axel.wernicke@gmx.de 030126 TODO sometimes we get an array out of bound exception in here ...
				// axel.wernicke@gmx.de 030129 TODO much worse: this destroys xFExFF mark for unicode :(
				if ((in[i+1]&0xff)>=0xe0 || in[i+1]==0)
				{
					// next byte is %111xxxxx or %00000000,
					// we must unsynchronize
					
					// first, enlarge out by one element
					byte []tmp = new byte[out.length + 1];
					System.arraycopy(out, 0, tmp, 0, outpos);
					out = tmp;
					tmp = null;
					out[outpos++]=-1;
					out[outpos++]=0;
					out[outpos++]=in[i+1];
					
					// skip next byte, we have already written it
					i++;
					
					did_unsync = true;
				}
				else
				{
					// no unsynchronization necessary
					out[outpos++]=in[i];
				}
			}
			else
			{
				// no unsynchronization necessary
				out[outpos++]=in[i];
			}
			
		}
		
		if (did_unsync == true)
		{
			// we did some unsynchronization
			return out;
		}
		else
		{
			return null;
		}
		
	}
	
	
	/**
	 * Enables or disables use of padding (enabled by default)
	 *
	 * @param use_padding True if padding should be used
	 */
	public void setUsePadding(boolean use_padding)
	{
		if (this.use_padding != use_padding)
		{
			is_changed = true;
			this.use_padding = use_padding;
		}
	}
	
	
	/** Gets padding usage
	 *
	 * @return True if padding is used
	 */
	public boolean getUsePadding()
	{
		return use_padding;
	}
	
	
	/** Enables / disables use of CRC
	 *
	 * @param use_crc True if CRC should be used
	 */
	public void setUseCRC(boolean use_crc)
	{
		if (this.use_crc != use_crc)
		{
			is_changed = true;
			this.use_crc = use_crc;
		}
	}
	
	
	/**
	 * @return True if CRC is used
	 */
	public boolean getUseCRC()
	{
		return use_crc;
	}
	
	
	/** Enables / disables use of unsynchronization
	 *
	 * @param use_crc True if unsynchronization should be used
	 */
	public void setUseUnsynchronization(boolean use_unsynch)
	{
		if (this.use_unsynchronization != use_unsynch)
		{
			is_changed = true;
			this.use_unsynchronization = use_unsynch;
		}
	}
	
	
	/**
	 * @return True if unsynchronization should be used
	 */
	public boolean getUseUnsynchronization()
	{
		return use_unsynchronization;
	}
	
	
	/** Test if file already has an ID3v2 tag
	 *	axel.wernicke@gmx.de 030126 seems this method is not in use ...
	 * @return true if file has IDv2 tag
	 */
	public boolean hasTag()
	{
		// simplified by axel.wernicke@gmx.de 030103 begin
		//		if (header != null)
		//		{
		//			return false;
		//		}
		//		else
		//		{
		//			return true;
		//		}
		
		return (( header != null ) ? false : true);
		
		// simplified by axel.wernicke@gmx.de 030103 end
	}
	
	
	/**
	 * Get all frames
	 *
	 * @return <code>Vector</code> of all frames
	 * @exception NoID3v2TagException If file does not contain ID3v2 tag
	 */
	public Vector getFrames() throws NoID3v2TagException
	{
		if (frames == null)
		{
			throw new NoID3v2TagException();
		}
		
		return frames;
	}
	
	
	/**
	 * Return all frame with ID <code>id</code>
	 *
	 * @param id Frame ID
	 * @return Requested frames
	 * @exception NoID3v2TagException If file does not contain ID3v2Tag
	 * @exception ID3v2NoSuchFrameException If file does not contain requested ID3v2 frame
	 */
	public Vector getFrame(String id) throws NoID3v2TagException, ID3v2NoSuchFrameException
	{
		if (frames == null)
		{
			throw new NoID3v2TagException();
		}
		
		Vector res = new Vector();
		ID3v2Frame tmp;
		for (Enumeration e = frames.elements() ; e.hasMoreElements() ;)
		{
			tmp = (ID3v2Frame)e.nextElement();
			if (tmp.getID().equals(id))
			{
				res.addElement(tmp);
			}
		}
		
		if (res.size()==0)
		{
			// no frame found
			throw new ID3v2NoSuchFrameException();
		}
		else
		{
			return res;
		}
	}
	
	
	/**
	 * Add a frame
	 *
	 * @param frame Frame to add
	 */
	public void addFrame(ID3v2Frame frame)
	{
		if (frames == null)
		{
			frames = new Vector();
		}
		
		frames.addElement(frame);
		is_changed = true;
	}
	
	
	/**
	 * Remove a frame.
	 *
	 * @param frame Frame to remove
	 * @exception NoID3v2TagException If file does not contain ID3v2Tag
	 * @exception ID3v2NoSuchFrameException If file does not contain requested ID3v2 frame
	 */
	public void removeFrame(ID3v2Frame frame) throws NoID3v2TagException, ID3v2NoSuchFrameException
	{
		if (frames == null)
		{
			throw new NoID3v2TagException();
		}
		
		if (frames.removeElement(frame) == false)
		{
			throw new ID3v2NoSuchFrameException();
		}
		is_changed = true;
	}
	
	
	/**
	 * Remove all frames with a given id.
	 *
	 * @param id ID of frames to remove
	 * @exception NoID3v2TagException If file does not contain ID3v2Tag
	 * @exception ID3v2NoSuchFrameException If file does not contain requested ID3v2 frame
	 */
	public void removeFrame(String id) throws NoID3v2TagException, ID3v2NoSuchFrameException
	{
		if (frames == null)
		{
			throw new NoID3v2TagException();
		}
		
		ID3v2Frame tmp;
		boolean found = false; // will be true if at least one frame was found
		for (Enumeration e = frames.elements() ; e.hasMoreElements() ;)
		{
			tmp = (ID3v2Frame)e.nextElement();
			if (tmp.getID().equals(id))
			{
				frames.removeElement(tmp);
				found = true;
			}
		}
		
		if (found == false)
		{
			throw new ID3v2NoSuchFrameException();
		}
		is_changed = true;
	}
	
	
	/**
	 * Remove a spefic frames with a given id. A number is given to identify the frame
	 * if more than one frame exists
	 *
	 * @param id ID of frames to remove
	 * @param number Number of frame to remove (the first frame gets number 0)
	 * @exception NoID3v2TagException If file does not contain ID3v2Tag
	 * @exception ID3v2NoSuchFrameException If file does not contain requested ID3v2 frame
	 */
	public void removeFrame(String id, int number) throws NoID3v2TagException, ID3v2NoSuchFrameException
	{
		if (frames == null)
		{
			throw new NoID3v2TagException();
		}
		
		ID3v2Frame tmp;
		int count = 0; // Number of frames with id found so far
		boolean removed = false; // will be true if at least frame was removed
		for (Enumeration e = frames.elements() ; e.hasMoreElements() ;)
		{
			tmp = (ID3v2Frame)e.nextElement();
			if (tmp.getID().equals(id))
			{
				if (count == number)
				{
					frames.removeElement(tmp);
					removed = true;
				}
				else
				{
					count++;
				}
			}
		}
		
		if (removed == false)
		{
			throw new ID3v2NoSuchFrameException();
		}
		is_changed = true;
	}
	
	
	/**
	 * Remove all frames
	 */
	public void removeFrames()
	{
		if (frames != null)
		{
			frames = new Vector();
		}
	}
	
	
	/** Write changes to file
	 *
	 * @exception IOException If an I/O error occurs
	 */
	public void update() throws IOException
	{
		// don't write changes if not necessary
		if( !is_changed ) { return; }

		// create array of bytes from id3v2 frames
		byte[] bframes = convertFramesToArrayOfBytes();

		// unsynchronize frames if necessary
		boolean uses_unsynchronization = false;
		if( use_unsynchronization )
		{
			byte []uns_frames = unsynchronize(bframes);
			if(uns_frames != null)
			{
				uses_unsynchronization = true;
				bframes = uns_frames;
			}
		}

		// length of header + tags + padding in bytes
		int length_file = (header != null) ? (header.getTagSize() + 10) : 0;

		// disable extended headers since they cause some trouble...
		boolean use_extended_header = false;
		
		// calculate new length of id3v2 header, extended header and tags - without padding !
		int new_length =  ( use_extended_header ) ?	(new ID3v2ExtendedHeader( use_crc, 0, 0 )).getBytes().length + bframes.length + 10
																								: bframes.length + 10;
		
		// check if we can update the file inplace therefore we need
		// if more space is needed than provided or no padding should be used and
		// lengths do not mach exactly, create a temporary file
		boolean updateInplace = !( header == null
																|| ( header != null && new_length > length_file )
																|| ( use_padding == false && new_length != length_file ) );
		
		// prepare crc checksum if crc is used...
		int crc = 0;
		if( use_crc )
		{
			java.util.zip.CRC32 crc_calculator = new java.util.zip.CRC32();
			crc_calculator.update(bframes);
			crc = (int)crc_calculator.getValue();
		}

		// calculate padding size
		long padding = 0;
		if( updateInplace )
		{
			// we're writing to old file, fill remainder with padding
			padding = length_file - new_length;
		}
		else
		{
			// if we're writing to new file, use enough padding to make resulting file size a multiple of 2048 bytes
			// calculate resulting file size
			long res_file_size = file.length() - length_file + new_length;
			padding = (long)(Math.ceil(res_file_size / 2048) * 2048) + 2048 - res_file_size;
		}


		//
		// ----------------- create new extended ID3V2 HEADER ---------------------
		//
		ID3v2ExtendedHeader new_ext_header = (use_extended_header) 
																					? new ID3v2ExtendedHeader( use_crc, crc, new Long(padding).intValue() )	: null;
																					
		byte[] bext_header = (use_extended_header) ? new_ext_header.getBytes() : new byte[0];

		// unsynchronize extended header if necessary
		if( use_unsynchronization )
		{
			byte []uns_ext_header = unsynchronize(bext_header);
			if(uns_ext_header != null)
			{
				uses_unsynchronization = true;
				bext_header = uns_ext_header;
			}
		}


		//
		// ----------------- create new ID3V2 HEADER ---------------------
		//
		ID3v2Header new_header = new ID3v2Header( VERSION, REVISION, uses_unsynchronization, use_extended_header, false,
																							bext_header.length + bframes.length + (int)padding );
		byte[] bheader = new_header.getBytes();


		//
		// ----------------- write ID3V2 HEADER, EXTENDED HEADER, TAG FRAMES AND PADDING ---------------------
		//
		// open file to write data to ( can be temp or original file )
		File write_to = updateInplace ? file : pri.nightmare.utils.File.getTempFile("ID3", file);
		java.io.RandomAccessFile out = new java.io.RandomAccessFile( write_to, "rw" );
		out.write(bheader);
		out.write(bext_header);
		out.write(bframes);

		// write padding
		if( use_padding )
		{
			for( int i=0; i<padding; i++ )
			{
				out.write(0);
			}
		}

		// write rest of file if we are using a temporary file
		if( !updateInplace )
		{
			BufferedInputStream copy_out = new BufferedInputStream( new FileInputStream(file) );
			
			// go to first byte after ID3v2 tag
			if( header != null )
			{
				copy_out.skip(length_file - 1);
			}

			int localBufferSize = 32768;
			byte localBuffer[] = new byte[localBufferSize];	// init local buffer

			// copy as long as we get a full chunk
			long readBytes = copy_out.read(localBuffer);
			while( readBytes == localBufferSize )
			{
				out.write(localBuffer);
				readBytes = copy_out.read(localBuffer);
			}

			// write last couple of bytes
			for( int i = 0; i < readBytes; i++ )
			{
				out.write( localBuffer[i] );
			}
			
			// close source stream
			copy_out.close();

			// temp file: rename file to original filename
			// if temp file and file are in the same directory, we can rename
			boolean renamed = write_to.renameTo(file);
			if( !renamed )
			{
				// hell, we must copy
				// pri.nightmare.utils.File.copy(write_to.getAbsolutePath(), file.getAbsolutePath());
				de.axelwernicke.mypod.util.FileUtils.copy( write_to, file );

				// delete now or later
				if( !write_to.delete() ) { write_to.deleteOnExit(); }
			}
		}

		// close destination stream
		out.close();

		// update id3v2 object
		header = new_header;
		extended_header = new_ext_header;
		is_changed = false;
	}
	
	
	/********** Private variables **********/
	
	private File file;
	
	private ID3v2Header header;
	private ID3v2ExtendedHeader extended_header;
	private Vector frames;
	
	private boolean is_changed = false;
	private boolean use_padding = true;
	private boolean use_crc = true;
	private boolean use_unsynchronization = true;
	
	
	/********** Private methods **********/
	
	/**
	 * Read ID3v2 header from file <code>in</code>
	 */
	private void readHeader(InputStream in) throws NoID3v2HeaderException,
																									ID3v2IllegalVersionException,
																									IOException
	{
		header = new ID3v2Header(in);
	}
	
	
	/**
	 * Read extended ID3v2 header from input stream <tt>in</tt>
	 *
	 * @param in Input stream to read from
	 */
	private void readExtendedHeader(InputStream in) throws IOException
	{
		// in file pointer must be at correct position (header
		// has just been read)
		extended_header = new ID3v2ExtendedHeader(in);
	}
	
	
	/**
	 * Read ID3v2 frames from stream <tt>in</tt>
	 * Stream position must be set to beginning of frames
	 *
	 * @param in Stream to read from
	 *
	 */
	private void readFrames(InputStream in) throws IOException, ID3v2WrongCRCException, ID3v2DecompressionException
	{
		// steps to read frames:
		// 1) Read all frames as bytes (don't include padding if size of padding is
		//                              known, i.e. ext. header exists)
		// 2) If CRC is present, make CRC check on frames
		// 3) Convert bytes to ID3v2Frames
		
		//// read all frames as bytes
		// calculate number of bytes to be read
		int bytes_to_read;
		if (extended_header != null)
		{
			// ext. header exists
			bytes_to_read = header.getTagSize() - (extended_header.getSize() + 4) - extended_header.getPaddingSize();
			
			// FIXME axel.wernicke@gmx.de begin
			// check plausibility - failures can be occure, if there is a extended header flagged but not contained in the file
			if( bytes_to_read < 0 ) { bytes_to_read = header.getTagSize(); }
			// FIXME axel.wernicke@gmx.de end
			
		}
		else
		{
			// no ext. header, include padding
			bytes_to_read = header.getTagSize();
		}
		
		// read bytes
		byte []unsynch_frames_as_byte = new byte[bytes_to_read];
		in.read(unsynch_frames_as_byte);
		
		byte []frames_as_byte;
		if (header.getUnsynchronization())
		{
			// undo effects of unsynchronization
			frames_as_byte = synchronize(unsynch_frames_as_byte);
			if (frames_as_byte == null)
			{
				frames_as_byte = unsynch_frames_as_byte;
			}
		} else
		{
			frames_as_byte = unsynch_frames_as_byte;
		}
		
		//// CRC check
		if (extended_header != null && extended_header.hasCRC() == true)
		{
			// make CRC check
			// calculate crc of read frames (because extended header exists,
			// they contain no padding)
			
			java.util.zip.CRC32 crc_calculator = new java.util.zip.CRC32();
			crc_calculator.update(frames_as_byte);
			int crc = (int)crc_calculator.getValue();
			
			if ((int)crc != (int)extended_header.getCRC())
			{
				// crc mismatch
				//throw new ID3v2WrongCRCException();
			}
		}
		
		
		//// Convert bytes to ID3v2Frames
		frames = new Vector();
		
		ByteArrayInputStream bis = new ByteArrayInputStream(frames_as_byte);
		// read frames as long as there are bytes and we are not reading from padding
		// (indicated by invalid frame id)
		ID3v2Frame frame = null;
		boolean cont = true;
		while ((bis.available() > 0) && (cont == true))
		{
			frame = new ID3v2Frame(bis);
			
			if (frame.getID() == ID3v2Frame.ID_INVALID)
			{
				// reached end of frames
				cont = false;
			}
			else
			{
				frames.addElement(frame);
			}
		}
	}
	
	
	/**
	 * Convert all frames to an array of bytes
	 */
	private byte[] convertFramesToArrayOfBytes()
	{
		ID3v2Frame tmp = null;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream(500);
		
		for (Enumeration e = frames.elements() ; e.hasMoreElements() ;)
		{
			// fix begin by axel.wernicke@gmx.de 030126
			// sometimes we get an exception from tmp.getBytes - this is caused by empty or misencoded frames
			try
			{
				tmp = (ID3v2Frame)e.nextElement();
				byte frame_in_bytes[] = tmp.getBytes();
				out.write(frame_in_bytes, 0, frame_in_bytes.length);
			}
			catch(Exception ex){ ; /* nothing we can do here :( */ }
			// fix end
		}
		
		return out.toByteArray();
	}
}
