package com.audiolaby.persistence;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.Author;
import com.audiolaby.persistence.model.BaseModel;
import com.audiolaby.persistence.model.Category;
import com.audiolaby.persistence.model.Part;
import com.audiolaby.persistence.model.User;
import com.audiolaby.persistence.model.VoiceOver;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EBean(scope = Scope.Singleton)
public class LibraryDAO {
    public int getCount(Class clazz) {
        return new Select().from(clazz).execute().size();
    }

    public void clear(Class clazz) {
        try {
            new Delete().from(clazz).execute();
        } catch (Exception e) {
        }

    }

    public  <T extends Model> List<T> getMany(Class<T> type, Class foreign, Model model) {
        try {
            String foreignKey = Cache.getTableInfo(foreign).getTableName();
            return new Select().from(type).where(Cache.getTableName(type) + "." + foreignKey + "=?", model.getId()).execute();
        } catch (Exception e) {
            return null;
        }
    }

    public  <T extends Model> T getOne(Class<T> type, Class foreign, Model model) {
        try {
            String foreignKey = Cache.getTableInfo(foreign).getTableName();
            return new Select().from(type).where(Cache.getTableName(type) + "." + foreignKey + "=?", model.getId()).executeSingle();
        } catch (Exception e) {
            return null;
        }
    }

    public <T extends Model & Serializable> T get(Class<T> clazz, Integer code) {
        return get(clazz, "error", code);
    }

    public <T extends Model & Serializable> T get(Class<T> clazz, String pkColumn, Object code) {
        return (T) new Select().from(clazz).where(pkColumn.concat(" = ?"), code).executeSingle();
    }

    public <T extends Model & Serializable> List<BaseModel> getAll(Class<T> clazz) {
        return new Select().from(clazz).execute();
    }

    public <T extends Model & Serializable> List<BaseModel> getAll(Class<T> clazz, int offset, int limit) {
        return new Select().from(clazz).offset(offset).limit(limit).execute();
    }

    public <T extends Model & Serializable> List<BaseModel> getAll(Class<T> clazz, String whereColumn, Object arg) {
        List<BaseModel> list = new Select().from(clazz).where(whereColumn.concat(" = ?"), arg).execute();
        return list != null ? list : new ArrayList();
    }

    public <T extends Model & Serializable> List<BaseModel> getAll(Class<T> clazz, String whereColumn, Object arg, String orderBy) {
        List<BaseModel> list = new Select().from(clazz).where(whereColumn.concat(" = ?"), arg).orderBy(orderBy).execute();
        return list != null ? list : new ArrayList();
    }

    public <T extends Model & Serializable> List<BaseModel> getAll(Class<T> clazz, String whereColumn, String andColumn, Object whereArg, Object andArg) {
        List<BaseModel> list = new Select().from(clazz).where(whereColumn.concat(" = ?"), whereArg).and(andColumn.concat(" = ?"), andArg).execute();
        return list != null ? list : new ArrayList();
    }

    public <T extends Model & Serializable> List<BaseModel> getAll(Class<T> clazz, String whereColumn, String andColumn, Object whereArg, Object andArg, String orderBy) {
        List<BaseModel> list = new Select().from(clazz).where(whereColumn.concat(" = ?"), whereArg).and(andColumn.concat(" = ?"), andArg).orderBy(orderBy).execute();
        return list != null ? list : new ArrayList();
    }

    public <T extends Model & Serializable> List<BaseModel> get(Class<T> clazz, String likeName, int offset, int limit) {
        return new Select().from(clazz).offset(offset).limit(limit).as(clazz.getSimpleName()).where(clazz.getSimpleName().concat(".name LIKE ?"), "%".concat(likeName).concat("%")).orderBy(clazz.getSimpleName().concat(".name")).execute();
    }

    public User getUser() {
        try {
            return (User) new Select().from(User.class).executeSingle();
        } catch (Exception e) {
            return null;
        }
    }

    public List<AudioArticle> getAudioPost() {

        try {
            List<AudioArticle> list = new ArrayList<AudioArticle>();
            list=  new Select().from(AudioArticle.class).execute();




            for(AudioArticle audioArticle :list)
            {
                List<Part> listPart = new ArrayList<Part>();
                listPart=  getMany(Part.class,AudioArticle.class, audioArticle);

                audioArticle.setPostParts(listPart);
                audioArticle.setCategory(getOne(Category.class,AudioArticle.class, audioArticle));
                audioArticle.setAuthor(getOne(Author.class,AudioArticle.class, audioArticle));
                audioArticle.setVoiceOver(getOne(VoiceOver.class,AudioArticle.class, audioArticle));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<AudioArticle>();
        }
    }

    public void clearUserData() {
        ActiveAndroid.beginTransaction();
        clear(User.class);
      //  clear(AudioArticle.class);
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
    }
}
