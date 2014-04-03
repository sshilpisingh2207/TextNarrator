package com.scu.textnarrator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String>{
	
	private ArrayList<Bitmap> thumbnails;
	private ArrayList<String> title;
	private ArrayList<String> paths;
	Context context;
	
	public CustomAdapter(Context context, int layout, 
			ArrayList<String> title, ArrayList<Bitmap> thumbnails, ArrayList<String> paths) {
		super(context, layout, title);
		this.context=context;
		this.title = title;
		this.thumbnails= thumbnails;
		this.paths = paths;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		LayoutInflater inflater =  ((Activity) context).getLayoutInflater(); 
		if(convertView == null) {
			holder = new Holder();
			convertView =  inflater.inflate(R.layout.activity_customicon, null);
			holder.txtname =  (TextView)convertView.findViewById(R.id.label);
			holder.img = (ImageView) convertView.findViewById(R.id.icon);
			holder.fileSize = (TextView)convertView.findViewById(R.id.filesize);
			holder.filePath = (TextView)convertView.findViewById(R.id.pathinfo);
			convertView.setTag(holder);
		} else {
			holder = (Holder)convertView.getTag();
		}
		holder.txtname.setText(title.get(position));
		Bitmap b = thumbnails.get(position);
		if(b==null)
			holder.img.setImageResource(R.drawable.messagebox_warning);
		else
			holder.img.setImageBitmap(thumbnails.get(position));
		File f = new File(paths.get(position));
		String fileSize = null;
		if(f!=null && f.exists()) 
			fileSize= Utility.formattedFileSize(f.length());
		else
			fileSize = "error";
		holder.fileSize.setText(fileSize);
		holder.filePath.setText(paths.get(position));
		return convertView;				
	}
	
	class Holder {
		TextView txtname;
		ImageView img;
		TextView fileSize;
		TextView filePath;
	}
	
	
	

}
