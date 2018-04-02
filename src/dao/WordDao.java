package dao;

import domain.Word;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.javatuples.KeyValue;
import org.springframework.stereotype.Repository;

import javax.persistence.ParameterMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class WordDao extends BaseDao<Word> {

    public void callProc(List<KeyValue<String,Integer>> wordInfo,  int type, int dataId, int sysId) {
        if( wordInfo.size() == 0 ) {
            return;
        }
        String[] wordTextItems = wordInfo.stream().map( n->n.getKey() + "," + n.getValue() ).toArray(String[]::new);
        String wordText = String.join("|",wordTextItems);

        getHibernateTemplate().execute((Session session) -> {
            ProcedureCall procedureCall = session.createStoredProcedureCall("proc_name");
            procedureCall.registerParameter("content",String.class, ParameterMode.IN).bindValue(wordText);
            procedureCall.registerParameter("data_id",Integer.class, ParameterMode.IN).bindValue(dataId);
            procedureCall.registerParameter("type",Integer.class, ParameterMode.IN).bindValue(type);
            procedureCall.registerParameter("sys_id", Integer.class, ParameterMode.IN).bindValue(sysId);
            procedureCall.getOutputs();
            return null;
        });
    }

}
