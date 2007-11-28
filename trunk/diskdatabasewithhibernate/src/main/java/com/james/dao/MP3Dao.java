package com.james.dao;

import java.util.List;

import com.james.dao.hibernate.MP3SearchCriteria;
import com.james.fileItems.MP3;

public interface MP3Dao {

    public abstract void create(MP3 mp3);

    public abstract void delete(MP3 mp3);

    public abstract MP3 getMP3(long id);

    public abstract MP3 load(long fileId);

    public abstract void update(MP3 mp3);

    public abstract List search(MP3SearchCriteria criteria);

}