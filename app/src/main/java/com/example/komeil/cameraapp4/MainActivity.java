package com.example.camera;

import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;

import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.net.Uri;
import android.content.Intent;
import android.provider.MediaStore;
import java.io.File;
import android.os.Environment;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.graphics.Color;

public class MainActivity extends Activity {

	private static final String TAG = "CallCamera";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
		
	Uri fileUri = null;
	ImageView photoImage = null;

	private File getOutputPhotoFile() {
		
		  File directory = new File(Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_PICTURES), getPackageName());
		  
		  if (!directory.exists()) {
		    if (!directory.mkdirs()) {
		      Log.e(TAG, "Failed to create storage directory.");
		      return null;
		    }
		  }
		  
		  String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
		  
		  return new File(directory.getPath() + File.separator + "IMG_"  
		                    + timeStamp + ".jpg");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		photoImage = (ImageView) findViewById(R.id.photo_image);
	
		Button callCameraButton = (Button) 
		findViewById(R.id.button_callcamera);
	
		callCameraButton.setOnClickListener( new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = Uri.fromFile(getOutputPhotoFile());
				i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
		    if (resultCode == RESULT_OK) {
		      Uri photoUri = null;
		      if (data == null) {
		        // A known bug here! The image should have saved in fileUri
		        Toast.makeText(this, "Image saved successfully", 
		                       Toast.LENGTH_LONG).show();
		        photoUri = fileUri;
		      } else {
		        photoUri = data.getData();
		        Toast.makeText(this, "Image saved successfully in: " + data.getData(), 
		                       Toast.LENGTH_LONG).show();
		      }
		      showPhoto(photoUri.getPath());
		    } else if (resultCode == RESULT_CANCELED) {
		      Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
		    } else {
		      Toast.makeText(this, "Callout for image capture failed!", 
		                     Toast.LENGTH_LONG).show();
		    }
		  }
	}
	
	private void showPhoto(String photoUri) {
		  File imageFile = new File (photoUri);
		  if (imageFile.exists()){
		     Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		     BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bmp);
		     photoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
		     photoImage.setImageDrawable(drawable);
             photoImage = (ImageView) findViewById(R.id.photo_image);


             /* Bitmap manipulated = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
              //ImageView img;
              //img = (ImageView)findViewById(R.id.imageView1);
              //BitmapDrawable  abmp = (BitmapDrawable)photoImage.getDrawable();
              //Bitmap bmp = abmp.getBitmap();

              int red = 33;   //0.33
              int green = 59; //0.59
              int blue = 11;    //0.11

              for (int i = 0; i < bmp.getWidth(); i++) {
                  for (int j = 0; j < bmp.getHeight(); j++)
                    {
                    int p = bmp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);

                    r = (int) red * r;
                    g = (int) green * g;
                    b = (int) blue * b;
                    manipulated.setPixel(i, j, Color.rgb(r/100, g/100, b/100));
                  }
              }
              photoImage.setImageBitmap(manipulated);
*/
              photoImage.setOnTouchListener(new OnTouchListener() {
                  @Override
                  public boolean onTouch(View arg0, MotionEvent arg1) {
                      int touched_x = (int) arg1.getX();
                      int touched_y = (int) arg1.getY();

                      int image_left_edge   = arg0.getLeft();
                      int image_right_edge  = arg0.getRight();
                      int image_top_edge    = arg0.getTop();
                      int image_bottom_edge = arg0.getBottom();



                      if ((image_left_edge < touched_x) && ((image_right_edge > touched_x)))
                         if ((image_bottom_edge > touched_y) && ((image_top_edge < touched_y)))
                            {//return true;
                            TextView resultString = (TextView) findViewById(R.id.result_string);

                            //Get the pixel hexa value of the specific point touched by user

                             View container = findViewById(R.id.container);

                             container.setDrawingCacheEnabled(true);
                             container.buildDrawingCache();
                             Bitmap bitmap = container.getDrawingCache();


                             int pixelValue = bitmap.getPixel(touched_x, touched_y);

                             StringBuilder stringBuilder = new StringBuilder("Hexa-Value : ");
                             stringBuilder.append(Integer.toHexString(Color.red(pixelValue)));
                             stringBuilder.append(Integer.toHexString(Color.green(pixelValue)));
                             stringBuilder.append(Integer.toHexString(Color.blue(pixelValue)));
                             stringBuilder.append("\nRed : ");
                             stringBuilder.append(Color.red(pixelValue));
                             stringBuilder.append("\nGreen : ");
                             stringBuilder.append(Color.green(pixelValue));
                             stringBuilder.append("\nBlue : ");
                             stringBuilder.append(Color.blue(pixelValue));
							 stringBuilder.append("\nSkin : ");
							 String skinness;
							 if(isSkin(pixelValue))
								 skinness = "YES";
							 else
							 	 skinness = "NO";
							 stringBuilder.append(skinness);
							 stringBuilder.append("\nName : ");
							 stringBuilder.append(colorName(pixelValue));


								resultString.setText(stringBuilder.toString());
                            container.setDrawingCacheEnabled(false);
                            return true;
                            }

                      TextView resultString = (TextView) findViewById(R.id.result_string);

                      StringBuilder stringBuilder = new StringBuilder("FAILED!");
                      resultString.setText(stringBuilder.toString());

                      return false;
                  }




              });



		  }       
	}

	private String colorName(int pixelValue){
		String name = "UNKNOWN";

		int R = Color.red(pixelValue);
		int G = Color.green(pixelValue);
		int B = Color.blue(pixelValue);

		if (isColorSimilar(pixelValue, 0, 0, 0))
			name = "Black";
		else if (isColorSimilar(pixelValue, 255, 255, 255))
			name = "White";
		else if (Math.abs(R-G) < 20 && Math.abs(R-B) < 20 && Math.abs(G-B) < 20)
			name = "Near White";
		else if (isColorSimilar(pixelValue, 255, 0, 0))
			name = "Red";
		else if (isColorSimilar(pixelValue, 0, 255, 0))
			name = "Lime";
		else if (isColorSimilar(pixelValue, 0, 0, 255))
			name = "Blue";
		else if (isColorSimilar(pixelValue, 255, 255, 0))
			name = "Yellow";
		else if (isColorSimilar(pixelValue, 0, 255, 255))
			name = "Cyan";
		else if (isColorSimilar(pixelValue, 255, 228, 181))
			name = "Moccasin";
		else if (isColorSimilar(pixelValue, 128, 128, 128))
			name = "Golden Rod";
		else if (isColorSimilar(pixelValue, 240, 230, 140))
			name = "Khaki";




		return name;
	}

	private boolean isColorSimilar(int pixelValue, int R, int G, int B){
		int tolerance = 50;

		int r = Color.red(pixelValue);
		int g = Color.green(pixelValue);
		int b = Color.blue(pixelValue);

		if ((Math.abs(R - r) < tolerance) &&
			(Math.abs(G - g) < tolerance) &&
			(Math.abs(B - b) < tolerance))
			return true;
		else
			return false;
	}


	private boolean isSkin(int color) {
		int R = Color.red(color);
		int G = Color.green(color);
		int B = Color.blue(color);

		boolean skinness = false;
		if (R > 95 && G >40 && B > 20 && (max(R,G,B) - min(R,G,B) > 15) && (Math.abs(R-G) > 15) && R > G && R>B)
			skinness = true;

  		return skinness;
	}

	private int max(int a, int b, int c) {
		int max = a;
		if (b > max)
			max = b;
		if (c > max)
			max = c;
		return max;
	}

	private int min(int a, int b, int c) {
		int min = a;
		if (b < min)
			min = b;
		if (c < min)
			min = c;
		return min;
	}
}