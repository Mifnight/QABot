package cn.buptteam.api;

import cn.buptteam.utils.GetAnswers;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Created by bitholic on 16/9/6.
 */

@Path("/answers")
@Produces("text/plain;charset=utf-8")
public class Answers {
    @GET
    @Produces("application/json")
    public Response getAnswers(@DefaultValue("") @QueryParam("question") String question){
        try{
            HashMap<String, Double> answers = (HashMap)GetAnswers.getAnswersByKeyword(question);
            String output = new Gson().toJson(answers);
            return Response.status(Response.Status.OK).entity(output).build();
        }catch (Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
