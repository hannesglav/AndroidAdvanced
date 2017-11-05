package at.technikumwien.maps.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.MyApplication;
import at.technikumwien.maps.util.managers.Cancelable;

/**
 * Created by FH on 05.11.2017.
 */

public class SyncDrinkingFountainsJobService extends JobService {

    private static String TAG = SyncDrinkingFountainsJobService.class.getSimpleName();

    private Cancelable cancelable = null;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(TAG, "JobService started, jobId=" + jobParameters.getJobId());
        AppDependencyManager manager = ((MyApplication) getApplicationContext()).getAppDependencyManager();
        cancelable = manager.getSyncManager().syncDrinkingFountains(new JobServiceOnOperationSuccessFulCallback(this, jobParameters));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG, "JobService stopped, jobId=" + jobParameters.getJobId());
        if(cancelable != null && !cancelable.isCanceled()) { cancelable.cancel(); }
        cancelable = null;
        return true;
    }
}
