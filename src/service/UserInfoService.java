package service;

import dao.UserInfoDao;
import domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    @Autowired
    UserInfoDao userInfoDao;

    public void add(UserInfo userInfo) {
        userInfoDao.save(userInfo);
    }

    public void update( UserInfo userInfo) {
        userInfoDao.update(userInfo);
    }

    public UserInfo get(int id ) {
        return userInfoDao.get(id);
    }

    public void del( int id ) {
        UserInfo userInfo = get(id);
        if( userInfo != null) {
            userInfoDao.remove(userInfo);
        }
    }
}
