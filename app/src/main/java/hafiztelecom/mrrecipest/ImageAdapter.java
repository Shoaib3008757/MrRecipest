package hafiztelecom.mrrecipest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static java.security.AccessController.getContext;

/**
 * Created by User-10 on 13-Apr-17.
 */

public class ImageAdapter  extends BaseAdapter {
    private LayoutInflater mInflater;


    private int lastPosition = -1;

    Context context1;
    private Integer[] imgid;

    public ImageAdapter(Context context, Integer[] id)
    {
        mInflater = LayoutInflater.from(context);
        imgid = id;
        context1 = context;
    }

    public int getCount()
    {
        return imgid.length;
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.image_list_layout, null);
            holder = new ViewHolder();
            holder.img =(ImageView) convertView.findViewById(R.id.recipe_image);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setImageResource(imgid[position]);

        Animation animation = AnimationUtils.loadAnimation(context1, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;

        return convertView;
    }

    static class ViewHolder
    {
        ImageView img;
    }}