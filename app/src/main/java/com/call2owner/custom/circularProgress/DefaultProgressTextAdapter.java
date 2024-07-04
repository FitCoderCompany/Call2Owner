package com.call2owner.custom.circularProgress;

public final class DefaultProgressTextAdapter implements CircularProgressIndicator.ProgressTextAdapter {

    @Override
    public String formatText(double currentProgress) {
        return String.valueOf((int) currentProgress);
    }
}
