package accessible.com.accesssound.utils;

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

import accessible.com.accessibilitysound.R;

public class DataAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    private static List<Noise> noiseArrayList;
    private static final String PREFS = "soundPref";
    private static final String PREF_NAME = "soundLogList";

    public DataAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        noiseArrayList = new ArrayList<>();

        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString(PREF_NAME, "");
        Type collectionType = new TypeToken<List<Noise>>(){}.getType();
        List<Noise> obj = gson.fromJson(json, collectionType);

        if (obj != null) {
            int startIndex = obj.size() - 1;
            for (int i = startIndex; i>=0; i--) {
                noiseArrayList.add((Noise)obj.get(i));
            }
            /*for (Object item : obj.toArray()) {
                noiseArrayList.add((Noise)item);
            }*/
        }
    }

    @Override
    public int getCount() {
        int size = noiseArrayList.size();
        return size;
    }

    @Override
    public Noise getItem(int position) {
        return noiseArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.noise_log_layout, null);

            holder = new ViewHolder();
            holder.timeStampTextBox = (TextView) convertView.findViewById(R.id.timeStampTextBox);
            holder.originalValueTextBox = (TextView) convertView.findViewById(R.id.originalValueTextBox);
            holder.roundedValueTextBox = (TextView) convertView.findViewById(R.id.roundedValueTextBox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Noise noise = getItem(position);

        if (noise != null) {
            holder.timeStampTextBox.setText("Time stamp: " + noise.getTimeStamp());
            holder.originalValueTextBox.setText("Original value: " + noise.getOriginalNoise());
            holder.roundedValueTextBox.setText("Rounded value: " + noise.getRoundedNoise());
        }

        return convertView;
    }

    public void updateList(List<Noise> pNoiseArrayList) {
        noiseArrayList = pNoiseArrayList;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView timeStampTextBox;
        public TextView originalValueTextBox;
        public TextView roundedValueTextBox;
    }
}
