package com.adolf.zhouzhuang.databasehelper;

import android.text.TextUtils;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.SpotsDao;
import java.util.List;
import de.greenrobot.dao.query.WhereCondition;

public class SpotsDataBaseHelper {
    private SpotsDao mSpotsDao;

    public SpotsDataBaseHelper(SpotsDao spotsDao) {
        this.mSpotsDao = spotsDao;
    }

    //插入全部
    public void insertAllSpotsList(List<Spots> spotsList){
        deleteAll();
        mSpotsDao.insertInTx(spotsList);
    }
    //删除全部
    public void deleteAll(){
        mSpotsDao.deleteAll();
    }
    //获取总数
    public long getSpotsCount(){
        return mSpotsDao.queryBuilder().count();
    }

    //获取全部景点
    public List<Spots> getAllSpots(){
       return mSpotsDao.loadAll();
    }

    //通过景点名获取景点信息
    public Spots getSpotsByName(String name){
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        WhereCondition whereCondition = SpotsDao.Properties.Title.eq(name);
        List<Spots> spotsList =  querySpots(whereCondition);
        return findFirst(spotsList);
    }

    public void updateSpots(Spots spots){
        mSpotsDao.update(spots);
    }


    //内部方法，不需要在外面调用
    private List<Spots> querySpots(WhereCondition whereCondition){

        return  mSpotsDao.queryBuilder().where(whereCondition).list();
    }

    //内部方法，不需要在外面调用
    private Spots findFirst(List<Spots> spotsList){
        if (spotsList == null || spotsList.size() == 0){
            return null;
        }else{
            return spotsList.get(0);
        }
    }

}
