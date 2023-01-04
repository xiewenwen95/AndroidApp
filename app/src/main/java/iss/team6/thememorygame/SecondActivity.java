package iss.team6.thememorygame;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class SecondActivity extends AppCompatActivity {

    private GameLogic gameLogic;
    public class ImageClickListener implements View.OnClickListener {
      @Override
      public void onClick(View view) {
          int [] coordinate=(int[])view.getTag();
          int x=coordinate[0];
          int y=coordinate[1];
          Card card=gameLogic.getCardByPosition(x,y);
          String showCard=card.getImageName();
          ImageView imageView = (ImageView) view;
          int drawableId=getResources().getIdentifier(showCard,"drawable",getPackageName());
          imageView.setImageResource(drawableId);

      }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] ImageNameList=new String[]{"apple_pic","banana_pic","cherry_pic","orange_pic",
                "pear_pic", "pineapple_pic"};//暂时写死，等待秉馨传输名称
        gameLogic=new GameLogic(ImageNameList);
        setContentView(R.layout.activity_second);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        final int childCount=gridLayout.getChildCount();
        for(int i=0;i<childCount;i++)
        {
            int [] position={i/gridLayout.getColumnCount(),i%gridLayout.getColumnCount()};
            ImageView imageView=(ImageView)gridLayout.getChildAt(i);
            imageView.setOnClickListener(new ImageClickListener());
            imageView.setTag(position);
        }

    }
}