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