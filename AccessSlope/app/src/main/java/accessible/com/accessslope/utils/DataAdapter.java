package accessible.com.accessslope.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import accessible.com.accessslope.R;

public class DataAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    private static List<Slope> resultArrayList;
    private static final String PREFS = "slopePref";
    private static final String PREF_NAME = "slopeLogList";

    public DataAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        resultArrayList = new ArrayList<>();

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(PREF_NAME, "");
        Type collectionType = new TypeToken<List<Slope>>(){}.getType();
        List<Slope> obj = gson.fromJson(json, collectionType);

        if (obj != null) {
            int startIndex = obj.size() - 1;
            for (int i = startIndex; i>=0; i--) {
                resultArrayList.add((Slope)obj.get(i));
            }
            /*for (Object item : obj.toArray()) {
                resultArrayList.add((Slope)item);
            }*/
        }
    }

    @Override
    public int getCount() {
        int size = resultArrayList.size();
        return size;
    }

    @Override
    public Slope getItem(int position) {
        return resultArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.slope_log_layout, null);

            holder = new ViewHolder();
            holder.timeStampTextBox = (TextView) convertView.findViewById(R.id.timeStampTextBox);
            holder.azimuthValueTextBox = (TextView) convertView.findViewById(R.id.azimuthValueTextBox);
            holder.pitchValueTextBox = (TextView) convertView.findViewById(R.id.pitchValueTextBox);
            holder.rollValueTextBox = (TextView) convertView.findViewById(R.id.rollValueTextBox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Slope slope = getItem(position);

        if (slope != null) {
            holder.timeStampTextBox.setText("Time stamp: " + slope.getTimeStamp());
            holder.azimuthValueTextBox.setText("Azimuth: " + slope.getAzimuth());
            holder.pitchValueTextBox.setText("Pitch: " + slope.getPitch());
            holder.rollValueTextBox.setText("Roll: " + slope.getRoll());
        }

        return convertView;
    }

    public void updateList(List<Slope> pResultArrayList) {
        resultArrayList = pResultArrayList;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView timeStampTextBox;
        public TextView azimuthValueTextBox;
        public TextView pitchValueTextBox;
        public TextView rollValueTextBox;
    }
}
