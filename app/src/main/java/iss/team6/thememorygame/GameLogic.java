package iss.team6.thememorygame;

import java.util.ArrayList;
import java.util.Collections;

public class GameLogic {
    private Card [][] shuffleCards = new Card[4][3];

    public GameLogic(String[] imageNameList) {
        initCard(imageNameList);
    }

    public void initCard(String[] imageNameList)
    {
        ArrayList<Card> cards=new ArrayList<Card>();
        cards.clear();
        int id = 0;
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<imageNameList.length;j++)
            {
                Card card=new Card();
                card.ImageName=imageNameList[j];
                card.ImageId=id++;
                cards.add(card);
            }
        }
        Collections.shuffle(cards);
        for(int i=0;i<cards.size();i++)
        {
            Card temp= cards.get(i);
            shuffleCards[i/shuffleCards[0].length][i% shuffleCards[0].length]= temp;
        }
    }

    public  Card getCardByPosition(int x,int y)
    {
        Card card=shuffleCards[x][y];
        return card;
    }
}
