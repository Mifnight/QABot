package cn.buptteam.evaluation;

/**
 * Created by 鳌天 on 2016/10/15.
 */
//采用威尔逊区间来评估答案质量
public class Evaluatiom {
    private int num_of_submit;
    private double p;
    private double weight = 0.5;
    private double init_score;
    private static final double Z = 1.96;
    private final String question;

    public static void main(String[] args){
        Evaluatiom evaluation = new Evaluatiom("asdasd",500,10,0.63);
        System.out.println(evaluation.getFinalScore());
    }

    public Evaluatiom(String question,int agree, int against, double init_score){
        this.question = question;
        this.num_of_submit = agree + against;
        this.p =  ((double)agree) / ((double)this.num_of_submit);
        System.out.println(this.p);
        this.init_score = init_score;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    private double getUserScore(){
        double score;
        if(this.num_of_submit != 0){
            score = (p + Z*Z/(2*this.num_of_submit) - Z*Math.sqrt(
                    (this.p*(1-this.p) + Z*Z/(4*this.num_of_submit))/this.num_of_submit
            ))/(1 + Z*Z/this.num_of_submit);
            System.out.println(score);
            return score;
        }
        else{
            return 0;
        }
    }

    public double getFinalScore(){
        double user_score = this.getUserScore();
        //System.out.println(user_score);
        double init_score = this.init_score;
        return user_score==0 ? init_score :
                (user_score*this.weight + init_score*(1-this.weight));
    }
}
