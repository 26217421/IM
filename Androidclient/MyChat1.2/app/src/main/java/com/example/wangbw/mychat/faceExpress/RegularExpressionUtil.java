package com.example.wangbw.mychat.faceExpress;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegularExpressionUtil {
    private static Context context;
    public static void init(Context c)
    {
        if(context == null)
        {
            context = c;
        }
    }

    public static SpannableString change(String content){
        //创建一个匹配模式
        //   \->\\     [->\[       [\u4e00-\u9fa5]表示中文
        //这个正则表达式用于检测以[]为边界，[]里面有一个或以上中文的字符串
        Pattern pattern = Pattern.compile("\\[[\u4e00-\u9fa5]{1,}\\]");
        SpannableString ss = new SpannableString(content);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find())
        {
            int position = check(matcher.group());
            if(position != -1)
            {
                Drawable d = context.getResources().getDrawable(Expression.drawable[position]);
                d.setBounds(0, 0, d.getIntrinsicWidth()/2, d.getIntrinsicHeight()/2);
                ImageSpan span = new ImageSpan(d);
                //以aaa[呵呵]bbb为例
                //start-3，end-7;setSpan不包括起点，包括终点
                ss.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
    }

    //检测是否有表情
    private static int check(String s)
    {
        for (int i = 0; i < Expression.describe.length; i++)
        {
            if(s.equalsIgnoreCase(Expression.describe[i]))
            {
                return i;
            }
        }
        return -1;
    }
}
