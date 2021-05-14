package ua.kpi.comsys.iv8224.pms.lab5;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ua.kpi.comsys.iv8224.pms.R;

public class Lab5 extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 2;

    private View root;
    private ScrollView scrollView;
    private LinearLayout scrollMain;
    private ArrayList<ImageView> allImages;
    private ArrayList<ArrayList<Object>> placeholderList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.lab5, container, false);

        scrollView = root.findViewById(R.id.scrollview_gallery);
        scrollMain = root.findViewById(R.id.linear_main);

        allImages = new ArrayList<>();
        placeholderList = new ArrayList<>();

        Button btnAddImage = root.findViewById(R.id.button_add_img);
        btnAddImage.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
            gallery.setType("image/*");
            startActivityForResult(gallery, RESULT_LOAD_IMAGE);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK){
            Uri imageUri = data.getData();
            addImage(scrollMain, allImages, placeholderList, scrollView, imageUri);
        }
    }

    private void addImage(LinearLayout scrollMain,
                          ArrayList<ImageView> allImages,
                          ArrayList<ArrayList<Object>> placeholderList,
                          ScrollView scrollView, Uri imageUri) {

        ImageView newImage = new ImageView(root.getContext());
        newImage.setImageURI(imageUri);
        newImage.setBackgroundColor(Color.BLACK);
        ConstraintLayout.LayoutParams imageParams =
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                        ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        imageParams.setMargins(3, 3, 3, 3);
        imageParams.dimensionRatio = "1";
        newImage.setLayoutParams(imageParams);
        newImage.setId(newImage.hashCode());

        ConstraintLayout tmpLayout = null;
        ConstraintSet tmpSet = null;
        if (allImages.size() > 0) {
            tmpLayout = (ConstraintLayout) getConstraintArrayList(0, placeholderList);
            tmpSet = (ConstraintSet) getConstraintArrayList(1, placeholderList);

            tmpSet.clone(tmpLayout);

            tmpSet.setMargin(newImage.getId(), ConstraintSet.START, 3);
            tmpSet.setMargin(newImage.getId(), ConstraintSet.TOP, 3);
            tmpSet.setMargin(newImage.getId(), ConstraintSet.END, 3);
            tmpSet.setMargin(newImage.getId(), ConstraintSet.BOTTOM, 3);
        }

        if (allImages.size() % 10 != 0)
            tmpLayout.addView(newImage);

        switch (allImages.size() % 10){
            case 0:{
                placeholderList.add(new ArrayList<>());

                ConstraintLayout ConstLayout = new ConstraintLayout(root.getContext());
                placeholderList.get(placeholderList.size()-1).add(ConstLayout);
                ConstLayout.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollMain.addView(ConstLayout);

                Guideline v_25 = makeGuideline(ConstraintLayout.LayoutParams.VERTICAL, 0.25f);
                Guideline v_50 = makeGuideline(ConstraintLayout.LayoutParams.VERTICAL, 0.50f);
                Guideline v_75 = makeGuideline(ConstraintLayout.LayoutParams.VERTICAL, 0.75f);
                Guideline h_25 = makeGuideline(ConstraintLayout.LayoutParams.HORIZONTAL, 0.25f);
                Guideline h_50 = makeGuideline(ConstraintLayout.LayoutParams.HORIZONTAL, 0.5f);
                Guideline h_75 = makeGuideline(ConstraintLayout.LayoutParams.HORIZONTAL, 0.75f);

                ConstLayout.addView(v_25, 0);
                ConstLayout.addView(v_50, 1);
                ConstLayout.addView(v_75, 2);
                ConstLayout.addView(h_25, 3);
                ConstLayout.addView(h_50, 4);
                ConstLayout.addView(h_75, 5);
                ConstLayout.addView(newImage);

                ConstraintSet newConstraintSet = new ConstraintSet();
                placeholderList.get(placeholderList.size()-1).add(newConstraintSet);
                newConstraintSet.clone(ConstLayout);

                newConstraintSet.connect(newImage.getId(), ConstraintSet.START,
                        ConstraintSet.PARENT_ID, ConstraintSet.START);
                newConstraintSet.connect(newImage.getId(), ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                newConstraintSet.connect(newImage.getId(), ConstraintSet.END,
                        v_25.getId(), ConstraintSet.START);
                newConstraintSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        h_25.getId(), ConstraintSet.TOP);

                newConstraintSet.applyTo(ConstLayout);
                break;
            }

            case 1: {
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(0).getId(), 0.25f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(2).getId(), 0.75f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(4).getId(), 0.5f);

                tmpSet.connect(newImage.getId(), ConstraintSet.START,
                        tmpLayout.getChildAt(0).getId(), ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                tmpSet.connect(newImage.getId(), ConstraintSet.END,
                        tmpLayout.getChildAt(2).getId(), ConstraintSet.START);
                tmpSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        tmpLayout.getChildAt(4).getId(), ConstraintSet.TOP);
                tmpSet.applyTo(tmpLayout);
                break;
            }

            case 2: {
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(2).getId(), 0.75f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(3).getId(), 0.25f);

                tmpSet.connect(newImage.getId(), ConstraintSet.START,
                        tmpLayout.getChildAt(2).getId(), ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                tmpSet.connect(newImage.getId(), ConstraintSet.END,
                        ConstraintSet.PARENT_ID, ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        tmpLayout.getChildAt(3).getId(), ConstraintSet.TOP);
                tmpSet.applyTo(tmpLayout);
                break;
            }

            case 3: {
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(0).getId(), 0.25f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(3).getId(), 0.25f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(4).getId(), 0.5f);

                tmpSet.connect(newImage.getId(), ConstraintSet.START,
                        ConstraintSet.PARENT_ID, ConstraintSet.START);
                tmpSet.connect(newImage.getId(), ConstraintSet.TOP,
                        tmpLayout.getChildAt(3).getId(), ConstraintSet.BOTTOM);
                tmpSet.connect(newImage.getId(), ConstraintSet.END,
                        tmpLayout.getChildAt(0).getId(), ConstraintSet.START);
                tmpSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        tmpLayout.getChildAt(4).getId(), ConstraintSet.TOP);
                tmpSet.applyTo(tmpLayout);
                break;
            }

            case 4: {
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(2).getId(), 0.75f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(3).getId(), 0.25f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(4).getId(), 0.5f);

                tmpSet.connect(newImage.getId(), ConstraintSet.START,
                        tmpLayout.getChildAt(2).getId(), ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.TOP,
                        tmpLayout.getChildAt(3).getId(), ConstraintSet.BOTTOM);
                tmpSet.connect(newImage.getId(), ConstraintSet.END,
                        ConstraintSet.PARENT_ID, ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        tmpLayout.getChildAt(4).getId(), ConstraintSet.TOP);
                tmpSet.applyTo(tmpLayout);
                break;
            }

            case 5: {
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(1).getId(), 0.5f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(4).getId(), 0.5f);

                tmpSet.connect(newImage.getId(), ConstraintSet.START,
                        ConstraintSet.PARENT_ID, ConstraintSet.START);
                tmpSet.connect(newImage.getId(), ConstraintSet.TOP,
                        tmpLayout.getChildAt(4).getId(), ConstraintSet.BOTTOM);
                tmpSet.connect(newImage.getId(), ConstraintSet.END,
                        tmpLayout.getChildAt(1).getId(), ConstraintSet.START);
                tmpSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                tmpSet.applyTo(tmpLayout);
                break;
            }


            case 6: {
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(1).getId(), 0.5f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(2).getId(), 0.75f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(4).getId(), 0.5f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(5).getId(), 0.75f);

                tmpSet.connect(newImage.getId(), ConstraintSet.START,
                        tmpLayout.getChildAt(1).getId(), ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.TOP,
                        tmpLayout.getChildAt(4).getId(), ConstraintSet.BOTTOM);
                tmpSet.connect(newImage.getId(), ConstraintSet.END,
                        tmpLayout.getChildAt(2).getId(), ConstraintSet.START);
                tmpSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        tmpLayout.getChildAt(5).getId(), ConstraintSet.TOP);
                tmpSet.applyTo(tmpLayout);
                break;
            }

            case 7: {
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(2).getId(), 0.75f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(4).getId(), 0.5f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(5).getId(), 0.75f);

                tmpSet.connect(newImage.getId(), ConstraintSet.START,
                        tmpLayout.getChildAt(2).getId(), ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.TOP,
                        tmpLayout.getChildAt(4).getId(), ConstraintSet.BOTTOM);
                tmpSet.connect(newImage.getId(), ConstraintSet.END,
                        ConstraintSet.PARENT_ID, ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        tmpLayout.getChildAt(5).getId(), ConstraintSet.TOP);
                tmpSet.applyTo(tmpLayout);
                break;
            }

            case 8: {
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(1).getId(), 0.5f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(2).getId(), 0.75f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(5).getId(), 0.75f);

                tmpSet.connect(newImage.getId(), ConstraintSet.START,
                        tmpLayout.getChildAt(1).getId(), ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.TOP,
                        tmpLayout.getChildAt(5).getId(), ConstraintSet.BOTTOM);
                tmpSet.connect(newImage.getId(), ConstraintSet.END,
                        tmpLayout.getChildAt(2).getId(), ConstraintSet.START);
                tmpSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                tmpSet.applyTo(tmpLayout);
                break;
            }

            case 9: {
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(2).getId(), 0.75f);
                tmpSet.setGuidelinePercent(tmpLayout.getChildAt(5).getId(), 0.75f);

                tmpSet.connect(newImage.getId(), ConstraintSet.START,
                        tmpLayout.getChildAt(2).getId(), ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.TOP,
                        tmpLayout.getChildAt(5).getId(), ConstraintSet.BOTTOM);
                tmpSet.connect(newImage.getId(), ConstraintSet.END,
                        ConstraintSet.PARENT_ID, ConstraintSet.END);
                tmpSet.connect(newImage.getId(), ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                tmpSet.applyTo(tmpLayout);
                break;
            }
        }

        allImages.add(newImage);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private Guideline makeGuideline(int orientation, float percent){
        Guideline guideline = new Guideline(root.getContext());
        guideline.setId(guideline.hashCode());

        ConstraintLayout.LayoutParams guideline_Params =
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        guideline_Params.orientation = orientation;

        guideline.setLayoutParams(guideline_Params);

        guideline.setGuidelinePercent(percent);

        return guideline;
    }

    private Object getConstraintArrayList(int index, ArrayList<ArrayList<Object>> list){
        return list.get(list.size()-1).get(index);
    }
}
