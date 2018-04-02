package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import common.ExecResult;
import org.javatuples.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import service.LexemeService;
import service.SystemService;

import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class Default {
    @Autowired
    LexemeService lexemeService;
    @Autowired
    SystemService systemService;

    @RequestMapping(value = "/welcome")
    public void welcome(PrintWriter pw) {
        pw.write("hello world");
    }

    @RequestMapping(value = "/create")
    public void create( PrintWriter pw, String text, String sysKey, int type, int dataId ){
        Integer sysId = systemService.getSystemId(sysKey);
        ExecResult rt;
        if( sysId == null) {
            rt = ExecResult.with(ExecResult.CODE_ERROR,"sysKey错误");
        } else {
            lexemeService.create(text,type,dataId,sysId);
            rt  = ExecResult.with(ExecResult.CODE_SUCCESS,"记录完成");
        }
        pw.println(rt.toJSON());
    }

    @RequestMapping(value = "/find")
    public void find(PrintWriter pw, String text, String sysKey, int type, int pageIndex, int pageSize) {
        Integer sysId = systemService.getSystemId(sysKey);
        ExecResult rt;
        if( sysId == null) {
            rt = ExecResult.with(ExecResult.CODE_ERROR,"sysKey错误");
        } else {
            KeyValue<Integer[], Integer> result = lexemeService.find(text,sysId,type,pageIndex,pageSize);
            Map<String,Object> data = new TreeMap<>();
            data.put("data_count", result.getValue());
            data.put("data_list", result.getKey());
            rt  = ExecResult.with(ExecResult.CODE_SUCCESS,"");
            rt.setData(data);
        }
        pw.println(rt.toJSON());
    }
}
