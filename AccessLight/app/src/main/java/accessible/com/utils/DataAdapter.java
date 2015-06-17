package accessible.com.utils;

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

import accessible.com.accesslight.R;

public class DataAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    private static List<Light> lightIntensityArrayList;
    private static final String PREFS = "lightPref";
    private static final String PREF_NAME = "lightLogList";

    public DataAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        lightIntensityArrayList = new ArrayList<>();

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(PREF_NAME, "");
        Type collectionType = new TypeToken<List<Light>>(){}.getType();
        List<Light> obj = gson.fromJson(json, collectionType);

        if (obj != null) {
            int startIndex = obj.size() - 1;
            for (int i = startIndex; i>=0; i--) {
                lightIntensityArrayList.add((Light)obj.get(i));
            }
        }
    }

    @Override
    public int getCount() {
        int size = lightIntensityArrayList.size();
        return size;
    }

    @Override
    public Light getItem(int position) {
        return lightIntensityArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.intensity_log_layout, null);

            holder = new ViewHolder();
            holder.timeStampTextBox = (TextView) convertView.findViewById(R.id.timeStampTextBox);
            holder.intensityValueTextBox = (TextView) convertView.findViewById(R.id.intensityValueTextBox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Light light = getItem(position);

        if (light != null) {
            holder.timeStampTextBox.setText("Time stamp: " + light.getTimeStamp());
            holder.intensityValueTextBox.setText("Intensity value: " + light.getLightIntensity());
        }

        return convertView;
    }

    public void updateList(List<Light> pLightIntensityArrayList) {
        lightIntensityArrayList = pLightIntensityArrayList;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView timeStampTextBox;
        public TextView intensityValueTextBox;
    }
}
