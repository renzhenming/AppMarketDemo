1.HttpUtils:

HttpUtils httpUtils = HttpUtils.with(this)
                .exchangeEngine(new OkHttpEngine())
                .cache(true)
                .url("http://is.snssdk.com/2/essay/discovery/v3/")
                .addParams("iid","6152551759")
                .addParams("aid","7")
                .execute(new HttpCallBack<String>() {

                    @Override
                    protected void onPreExecute() {

                    }

                    @Override
                    public void onError(final Exception e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess(final String result) {
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloadProgress(int progress) {

                    }

                    @Override
                    public void onUploadProgress(int progress) {

                    }

                });

2.CommonDialog:

CommonDialog dialog = new CommonDialog.Builder(TestActivity.this)
        .setContentView(R.layout.dialog)
        .setText(R.id.toast,"我是新的dialog")
        .fullWidth()
        // 可以设置宽度占屏幕百分比  widthPercent(0.9f)
        .alignBottom(true)
        .show();

        //我要获取到输入框的值，可以这样做 getView  (ListView RecyclerView CheckBox)
        final EditText mEditText = dialog.getView(输入框的id);
        dialog.setOnClickListener(R.id.toast, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),mEditText.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

3.CommonNavigationBar:

CommonNavigationBar navigationBar = new CommonNavigationBar.Builder(this)
       .setTitle("压缩图片")
       .setRightText("确定")
       .setRightTextColor(R.color.green)
       .setRightTextSize(14)
       .setBackgroundColor(R.color.gray)
       .setRightClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
               Toast.makeText(getApplicationContext(),"确定",Toast.LENGTH_SHORT).show();
           }
       })
       .setLeftClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
               Toast.makeText(getApplicationContext(),"退出",Toast.LENGTH_SHORT).show();
           }
       })
       .setTitleTextColor(R.color.red)
       .setTitleTextSize(18)
       .build();

4.PermissionHelper:

    注意：使用PermissionHelper动态申请权限，最好是每次申请一个，每一个权限的申请单独使用一个回调方法，假如你同时申请多个权限，那么只有所有权限都
    授权成功，PermissionSucceed方法才会被调用，否则只要有一个权限没有通过，就不会被调用，而是会走到PermissionPermanentDenied或者PermissionDenied
    中，导致一些问题。即便是你要同时申请好几个权限，也要保证这几个权限都是相关的，即必须全部授权才能执行下一步

    PermissionHelper.with(this).requestCode(111).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO
        }).request();
    }

    @PermissionSucceed(requestCode = 111)
    public void onPermissionGranted(){
        Toast.makeText(getApplicationContext(),"授权成功",Toast.LENGTH_SHORT).show();

    }

    @PermissionPermanentDenied(requestCode = 111)
    public void PermissionPermanentDenied(String permission){
        Toast.makeText(getApplicationContext(),"您永久拒绝了权限"+permission+"，请去设置页面开启",Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(requestCode = 111)
    public void PermissionDenied(String permission){
        Toast.makeText(getApplicationContext(),"授权被拒绝:"+permission,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this,requestCode,permissions,grantResults);
    }