package com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite;


import com.tourism.my.tourismmanagement.db.db.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 涓?瀵瑰寤惰繜鍔犺浇绫??
 * Created by pwy on 13-7-25.
 * @param <O> 瀹夸富???炰綋鐨刢lass
 * @param <M> 澶氭斁???炰綋class
 */
public class OneToManyLazyLoader<O,M> {
    O ownerEntity;
    Class<O> ownerClazz;
    Class<M> listItemClazz;
    FinalDb db;
    public OneToManyLazyLoader(O ownerEntity,Class<O> ownerClazz,Class<M> listItemclazz,FinalDb db){
        this.ownerEntity = ownerEntity;
        this.ownerClazz = ownerClazz;
        this.listItemClazz = listItemclazz;
        this.db = db;
    }
    List<M> entities;

    /**
     * 濡傛灉鏁版嵁鏈姞杞斤紝鍒欒皟鐢╨oadOneToMany濉厖鏁版嵁
     * @return
     */
    public List<M> getList(){
        if(entities==null){
            this.db.loadOneToMany((O)this.ownerEntity,this.ownerClazz,this.listItemClazz);
        }
        if(entities==null){
            entities =new ArrayList<M>();
        }
        return entities;
    }
    public void setList(List<M> value){
        entities = value;
    }

}
