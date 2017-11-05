package at.technikumwien.maps.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import at.technikumwien.maps.data.OnOperationSuccessfulCallback;

final class JobServiceOnOperationSuccessFulCallback implements OnOperationSuccessfulCallback {

    private final JobService jobService;
    private final JobParameters jobParameters;

    JobServiceOnOperationSuccessFulCallback(JobService jobService, JobParameters jobParameters) {
        this.jobService = jobService;
        this.jobParameters = jobParameters;
    }

    @Override
    public void onOperationSuccessful() {
        Log.i(jobService.getClass().getSimpleName(), "Service executed successfully. jobId=" + jobParameters.getJobId());
        jobService.jobFinished(jobParameters, false);
    }

    @Override
    public void onOperationError(Throwable throwable) {
        Log.e(jobService.getClass().getSimpleName(), "Error on service execution. jobId=" + jobParameters.getJobId(), throwable);
        jobService.jobFinished(jobParameters, true);
    }
}
