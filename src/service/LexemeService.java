package service;
import dao.WordDao;
import dao.WordDicDao;
import domain.Word;
import org.javatuples.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//分词
@Service
public class LexemeService {
    @Autowired
    WordDao wordDao;
    @Autowired
    WordDicDao wordDicDao;

    /**
     *
     * @param text  产生词组的文本
     * @param type  类型，如人机问答、悬赏等等， 可以在LexemeType枚举中增加类型
     * @param dataId   文本所属记录的ID
     */
    public void create( String text, int type, int dataId, int sysId ) {

    }

    /**
     *
     * @param text   查找的文本
     * @param type    类型，如人机问答、悬赏等等， 可以在LexemeType枚举中增加类型
     * @param pageIndex   分页页码
     * @param pageSize    每页记录数
     * @return    查找的记录ID
     */
    public KeyValue<Integer[], Integer> find( String text, int sysId, int type, int pageIndex, int pageSize ) {
        return null;
    }
}
