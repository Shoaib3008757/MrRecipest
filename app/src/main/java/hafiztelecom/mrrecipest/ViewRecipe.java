package hafiztelecom.mrrecipest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class ViewRecipe extends AppCompatActivity {


//    ImageView viewRecipeMethod;

    TouchImageView img;



    long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        init();

        Intent i = getIntent();

       id =  i.getExtras().getLong("id", -1);

        if (id==0){
            img.setImageResource(R.drawable.bihari_korma_tareka);
        }
        if (id==1){
            img.setImageResource(R.drawable.teriyaki_chiken_tareka);
        }
        if (id==2){
            img.setImageResource(R.drawable.chiken_tikka_tareka);
        }
        if (id==3){
            img.setImageResource(R.drawable.fry_paratha_tareka);
        }
        if (id==4){
            img.setImageResource(R.drawable.gogappy_tareka);
        }
        if (id==5){
            img.setImageResource(R.drawable.mutton_biryani_tareka);
        }
        if (id==6){
            img.setImageResource(R.drawable.mutton_sindhi_biryani_tareka);
        }
        if (id==7){
            img.setImageResource(R.drawable.seekh_kabab_tareka);
        }
        if (id==8){
            img.setImageResource(R.drawable.shahi_mutton_karai_tareka);
        }
        if (id==9){
            img.setImageResource(R.drawable.allo_methis_tareka);
        }




    }

    public void init(){
        img = (TouchImageView) findViewById(R.id.view_recipe_method);

        RotateAnimation rotateAnimation = new RotateAnimation(2010.0f, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0.5f);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0.5f);


        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(3999);
        animationSet.setInterpolator(new OvershootInterpolator());
        img.startAnimation(animationSet);

    }
}
