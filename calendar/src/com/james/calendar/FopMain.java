package com.james.calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.apps.MimeConstants;

import com.james.calendar.element.FOPCalendar;
import com.james.calendar.util.CalendarCreator;
import com.james.calendar.util.FileOutputHelper;

public class FopMain {

    private static Fop createFop() throws FileNotFoundException, FOPException {
        String outputMIMEType = MimeConstants.MIME_PDF;
        FopFactory fopFactory = FopFactory.newInstance();

        FOUserAgent userAgent = fopFactory.newFOUserAgent();

        OutputStream os = getOutputFile();

        return fopFactory.newFop(outputMIMEType, userAgent, os);
    }

    private static StreamSource getFoFile() {
        FOPCalendar fo = CalendarCreator.newInstance().create(2007);

        String xml = fo.toString();

        String tempFile = "tempFile" + System.currentTimeMillis() + ".fo";

        FileOutputHelper.write(xml, tempFile);

        // System.out.println("XML: " + xml);

        StreamSource is = new StreamSource(new File(tempFile));
        return is;
    }

    private static OutputStream getOutputFile() {
        String outputFileName = "output.pdf";

        OutputStream os = null;

        try {
            os = new FileOutputStream(outputFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return os;
    }

    public static void main(String args[]) throws FileNotFoundException, FOPException, TransformerException {
        Fop fop = createFop();

        transform(fop);

        FormattingResults results = fop.getResults();

        System.out.println("Number of pages: " + results.getPageCount());
    }

    private static void transform(Fop fop) throws FOPException, TransformerException {
        // Resulting SAX events (the generated FO) must be piped through to FOP
        Result res = new SAXResult(fop.getDefaultHandler());

        // Setup JAXP using identity transformer
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        // Setup input stream
        Source src = getFoFile();

        // Start XSLT transformation and FOP processing
        transformer.transform(src, res);
    }
}