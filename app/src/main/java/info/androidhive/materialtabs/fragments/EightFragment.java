package info.androidhive.materialtabs.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import info.androidhive.materialtabs.R;


public class EightFragment extends IntermediateFragment{

    TextView tv_timeInterval = null;
    TextView tv_pwmOutMid = null;

    TextView tv_pwmOutMin = null;
    TextView tv_pwmOutMax = null;
    TextView tv_pwmOutSpeedLimit = null;

    Spinner sp_functions_stanby = null;
    Spinner sp_functions_recenterCamera = null;
    Spinner sp_irCameraControl = null;
    Spinner sp_cameraModel = null;

    Spinner sp_irCameraSetting1 = null;
    Spinner sp_irCameraSetting2 = null;


    public EightFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("Storm32", "FF7: onCreateView: ");
        View v =  inflater.inflate(R.layout.fragment_eight, container, false);

        removeControlsFromTables();

        tv_pwmOutMid = (TextView) v.findViewById(R.id.textView_functions_PwmOutMid);
        tv_timeInterval = (TextView) v.findViewById(R.id.textView_functions_timeInterval);

        tv_pwmOutMin = (TextView) v.findViewById(R.id.textView_functions_PwmOutMin);
        tv_pwmOutMax = (TextView) v.findViewById(R.id.textView_functions_PwmOutMix);

        tv_pwmOutSpeedLimit = (TextView) v.findViewById(R.id.textView_function_pwmOutSpeedLimit);


        sp_functions_stanby = (Spinner) v.findViewById(R.id.spinner_function_standby);
        sp_functions_recenterCamera = (Spinner) v.findViewById(R.id.spinner_functions_reCenterCamera);

        sp_irCameraControl = (Spinner) v.findViewById(R.id.spinner_functions_IrCameraControl);
        sp_cameraModel = (Spinner) v.findViewById(R.id.spinner_functions_cameraModel);

        sp_irCameraSetting1 = (Spinner) v.findViewById(R.id.spinner_functions_IrCameraSettingOne);
        sp_irCameraSetting2 = (Spinner) v.findViewById(R.id.spinner_functions_IrCameraSettingTwo);


        addPairTv(v, tv_timeInterval, 75);

        addPairTv(v, tv_pwmOutMid, 114);
        addPairTv(v, tv_pwmOutMin, 115);
        addPairTv(v, tv_pwmOutMax, 116);
        addPairTv(v, tv_pwmOutSpeedLimit, 117);

        addPairSpinner(v, sp_functions_stanby, 70);
        addPairSpinner(v, sp_functions_recenterCamera, 76);
        addPairSpinner(v, sp_irCameraControl, 71);
        addPairSpinner(v, sp_cameraModel, 72);

        addPairSpinner(v, sp_irCameraSetting1, 73);
        addPairSpinner(v, sp_irCameraSetting2, 74);

//        updateGUI();
        updateAllControlsAccordingToOptionList();

        return v;
    }


}
