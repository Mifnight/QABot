package cn.buptteam.api;

import cn.buptteam.utils.GetAnswers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by bitholic on 16/9/6.
 */

@Path("answers")
@Produces("text/plain")
public class Answers {
    @GET
    @Produces("text/html")
    public Response getAnswers(){
        String output = "";
        try{
            output = GetAnswers.getAnswersByKeyword("在路口右转弯遇同车道前车等候放行信号时如何行驶？").toString();
        }catch (Exception ex){
            output = "fail";
        }
        return Response.ok("yeah jersey server is running").build();
    }
}
