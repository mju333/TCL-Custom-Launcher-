package com.mju333.tcllauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ربط زر الإعدادات لفتح إعدادات التلفزيون
        View btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "تعذر فتح الإعدادات", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // ربط زر الرجوع للثيم الأصلي
        View btnOriginalTheme = findViewById(R.id.btnOriginalTheme);
        if (btnOriginalTheme != null) {
            btnOriginalTheme.setOnClickListener(v -> {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            });
        }

        // بناء وتعبئة الـ 10 أماكن المخصصة للبرامج في الشريط السفلي
        loadAppsDock();
    }

    private void loadAppsDock() {
        LinearLayout appsDock = findViewById(R.id.appsDock);
        if (appsDock == null) return;

        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> allApps = pm.queryIntentActivities(intent, 0);

        // إنشاء 10 أماكن مخصصة تماماً لطلبك
        for (int i = 0; i < 10; i++) {
            TextView slot = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            params.setMargins(8, 0, 8, 0);
            slot.setLayoutParams(params);
            slot.setGravity(Gravity.CENTER);
            slot.setTextColor(0xFFFFFFFF);
            slot.setTextSize(14f);

            final int index = i;
            if (i < allApps.size()) {
                ResolveInfo appInfo = allApps.get(i);
                CharSequence appLabel = appInfo.loadLabel(pm);
                slot.setText(appLabel);
                slot.setBackgroundColor(0x33FFFFFF);

                final String packageName = appInfo.activityInfo.packageName;
                final String className = appInfo.activityInfo.name;

                slot.setOnClickListener(v -> {
                    try {
                        Intent launchIntent = new Intent(Intent.ACTION_MAIN);
                        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        launchIntent.setClassName(packageName, className);
                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchIntent);
                    } catch (Exception e) {
                        Toast.makeText(this, "تعذر تشغيل التطبيق", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                slot.setText("+ " + (i + 1));
                slot.setBackgroundColor(0x15FFFFFF);
                slot.setOnClickListener(v -> Toast.makeText(this, "مكان مخصص رقم " + (index + 1), Toast.LENGTH_SHORT).show());
            }

            appsDock.addView(slot);
        }
    }

    @Override
    public void onBackPressed() {
        // منع الخروج من الثيم ليبقى هو الواجهة الافتراضية
    }
}
