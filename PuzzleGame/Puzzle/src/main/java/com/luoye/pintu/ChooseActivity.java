package com.luoye.pintu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class ChooseActivity extends Activity {

    private ImageButton image0,image1,image2;
    static int f =0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_img);
        image0 = (ImageButton) findViewById(R.id.image0);
        image1 = (ImageButton) findViewById(R.id.image1);
        image2 = (ImageButton) findViewById(R.id.image2);

        OnClickListener buttonListener = new OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, MainActivity.class);// 指定接下来要启动的class
                switch(v.getId()) {
                    case R.id.image0:{
                        f=0;
                        break;
                    }
                    case R.id.image1:{
                        f=1;
                        break;
                    }
//                    case R.id.image2:{
//                        Intent i = new Intent(
//                                Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(i, RESULT_LOAD_IMAGE);
//                        break;
//                    }
                }
                startActivity(intent);
            }
        };

        image0.setOnClickListener(buttonListener);
        image1.setOnClickListener(buttonListener);
    }



    public String getData(){
        System.out.println(f);
        if(f==1){
            return "back.jpg";
        }

        return "back1.jpg";
    }

}
