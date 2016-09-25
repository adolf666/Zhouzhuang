package com.adolf.zhouzhuang.databasehelper;

import com.adolf.zhouzhuang.Favorites;
import com.adolf.zhouzhuang.FavoritesDao;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.SpotsDao;

import java.util.List;

import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by adolf on 2016/9/25.
 */
public class FavoriteDataBaseHelper {
    private FavoritesDao mFavoriteListDao;

    public FavoriteDataBaseHelper(FavoritesDao favoriteDao) {
        this.mFavoriteListDao = favoriteDao;
    }

    public void deleteAll(){
        mFavoriteListDao.deleteAll();
    }

    public void addFavorite(Favorites favorites){
        mFavoriteListDao.insertInTx(favorites);
    }

    public  void deleteFavorite(Favorites favorites){
        mFavoriteListDao.delete(favorites);
    }

    public List<Favorites>  getFavoriteByPUserId(int userId){
        WhereCondition whereCondition = FavoritesDao.Properties.UserId.eq(userId);
        List<Favorites> spotsList =  queryFavorite(whereCondition);
        return spotsList;
    }

    public boolean isFavoriteByUserIdAndSpotsId(int userId,int soptsId){
        WhereCondition whereCondition = FavoritesDao.Properties.UserId.eq(userId);
        WhereCondition whereCondition2 = FavoritesDao.Properties.SpotsId.eq(soptsId);
        List<Favorites> favoritesList = mFavoriteListDao.queryBuilder().where(whereCondition).where(whereCondition2).list();
        if (favoritesList == null || favoritesList.size() == 0){
            return false;
        }
        return true;
    }

    public Favorites getFavoriteByUserIdAndSpotsId(int userId,int soptsId){
        WhereCondition whereCondition = FavoritesDao.Properties.UserId.eq(userId);
        WhereCondition whereCondition2 = FavoritesDao.Properties.SpotsId.eq(soptsId);
        List<Favorites> favoritesList = mFavoriteListDao.queryBuilder().where(whereCondition).where(whereCondition2).list();
        return findFirst(favoritesList);
    }


    //内部方法，不需要在外面调用
    private List<Favorites> queryFavorite(WhereCondition whereCondition){

        return  mFavoriteListDao.queryBuilder().where(whereCondition).list();
    }

    //内部方法，不需要在外面调用
    private Favorites findFirst(List<Favorites> favoriteList){
        if (favoriteList == null || favoriteList.size() == 0){
            return null;
        }else{
            return favoriteList.get(0);
        }
    }
}
