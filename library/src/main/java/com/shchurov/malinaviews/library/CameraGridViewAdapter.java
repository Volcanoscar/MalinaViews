package com.shchurov.malinaviews.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;

public abstract class CameraGridViewAdapter<T> extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<T> items;
    private Camera camera;
    private ViewGroup parent;

    public CameraGridViewAdapter(Context context, List<T> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size() + 1;
    }

    @Override
    public T getItem(int position) {
        return position == 0 ? null : items.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_CAMERA : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (this.parent == null) {
            this.parent = parent;
        }
        if (type == TYPE_CAMERA) {
            if (convertView == null) {
                convertView = createCameraView();
            }
            return convertView;
        }
        return getNormalView(position, convertView, parent);
    }

    private View createCameraView() {
        SquareFrameLayout fl = new SquareFrameLayout(context);
        fl.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        fl.setBackgroundColor(Color.BLACK);
        TextureView textureView = new TextureView(context);
        textureView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        fl.addView(textureView);
        textureView.setSurfaceTextureListener(new SurfaceListener(textureView));
        return fl;
    }

    public abstract View getNormalView(int position, View convertView, ViewGroup parent);

    private class SurfaceListener implements TextureView.SurfaceTextureListener {

        TextureView textureView;

        private SurfaceListener(TextureView textureView) {
            this.textureView = textureView;
        }

        @Override
        public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (CameraGridViewAdapter.this) {
                        if (camera == null) {
                            camera = Camera.open();
                            // need to be tested but 90 should be ok for most devices
                            camera.setDisplayOrientation(90);
                        }
                        try {
                            Camera.Parameters params = camera.getParameters();
                            Camera.Size previewSize = params.getPreviewSize();
                            // because of strange issue on Nexus devices
                            try {
                                params.setPictureSize(previewSize.width, previewSize.height);
                                camera.setParameters(params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int w = previewSize.width;
                            int h = previewSize.height;
                            final Matrix matrix = new Matrix();
                            if (w > h) {
                                matrix.setScale(1, (float) w / h);
                            } else {
                                matrix.setScale((float) h / w, 1);
                            }
                            parent.post(new Runnable() {
                                @Override
                                public void run() {
                                    textureView.setTransform(matrix);
                                }
                            });
                            camera.setPreviewTexture(surface);
                            camera.startPreview();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public synchronized boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            camera.stopPreview();
            camera.release();
            camera = null;
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }

    }

    private static class SquareFrameLayout extends FrameLayout {

        SquareFrameLayout(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }

}
