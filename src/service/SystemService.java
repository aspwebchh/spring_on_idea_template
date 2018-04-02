package service;

import dao.SystemDao;
import domain.System;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemService {
    @Autowired
    SystemDao systemDao;

    public Integer getSystemId( String sysKey ) {
        List<System> dataList = systemDao.find("from System where sysKey = ?", sysKey);
        if( dataList.size() == 0 ) {
            return null;
        } else {
            return dataList.get(0).getSysId();
        }
    }
}
