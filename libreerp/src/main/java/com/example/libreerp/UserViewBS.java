package com.example.libreerp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by MG on 17-07-2016.
 */
public class UserViewBS extends BottomSheetDialogFragment {

    int userPK;
    TextView username;
    ImageView userImage;
    int initialheight;
    boolean isExpanded = false;
    public static UserViewBS newInstance(int userPK) {
        UserViewBS f = new UserViewBS();
        Bundle args = new Bundle();
        args.putInt("userPK", userPK);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_view_bs, container, false);
        final RelativeLayout view = (RelativeLayout) v.findViewById(R.id.gradient);
        userPK = getArguments().getInt("userPK");
        final Users users = new Users(getContext());
        username = (TextView) v.findViewById(R.id.usernameBS);
        userImage = (ImageView) v.findViewById(R.id.userimageBS);
                users.get(userPK , new UserMetaHandler(){
                    @Override
                    public void onSuccess(UserMeta user){
                        System.out.println("yes65262626626");
                        username.setText(user.getFirstName() + " " + user.getLastName());
                    }
                    @Override
                    public void handleDP(Bitmap dp){
                        System.out.println("dp dsda");
                        userImage.setImageBitmap(dp);
                    }

                });
        initialheight = userImage.getLayoutParams().height;
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded == false) {
                    ResizeAnimation resizeAnimation = new ResizeAnimation(
                            userImage,
                            userImage.getWidth(),
                            userImage.getLayoutParams().height
                    );
                    resizeAnimation.setDuration(500);
                    userImage.startAnimation(resizeAnimation);
                    userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    view.setVisibility(View.INVISIBLE);
                    isExpanded = true;
                    v.invalidate();
                } else {
                    ResizeAnimation resizeAnimation = new ResizeAnimation(
                            userImage,
                            (int) getResources().getDimension(R.dimen.imageview_height),
                            userImage.getLayoutParams().height
                    );
                    resizeAnimation.setDuration(500);
                    userImage.startAnimation(resizeAnimation);
                    userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    isExpanded = false;
                    v.invalidate();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            view.setVisibility(View.VISIBLE);
                        }
                    }, 500);


                }
            }
            });

        final TextView mobile = (TextView) v.findViewById(R.id.mobileBS);
        mobile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String phone_no= mobile.getText().toString().replaceAll("-", "");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone_no));
                startActivity(intent);
            }
        });


        return v;
    }


    public class ResizeAnimation extends Animation {
        final int targetHeight;
        View view;
        int startHeight;

        public ResizeAnimation(View view, int targetHeight, int startHeight) {
            this.view = view;
            this.targetHeight = targetHeight;
            this.startHeight = startHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            int newHeight = (int) (startHeight + targetHeight * interpolatedTime);
            //to support decent animation, change new heigt as Nico S. recommended in comments
            int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
