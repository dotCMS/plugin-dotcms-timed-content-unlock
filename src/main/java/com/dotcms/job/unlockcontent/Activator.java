package com.dotcms.job.unlockcontent;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.quartz.CronTrigger;

import com.dotmarketing.osgi.GenericBundleActivator;
import com.dotmarketing.quartz.CronScheduledTask;
import com.dotmarketing.quartz.QuartzUtils;

public class Activator extends GenericBundleActivator {

    public final static String JOB_NAME = "Unlock locked content based on a timer";

    public final static String JOB_GROUP = "OSGi Jobs";
    public final static String CRON_EXPRESSION = "CRON_EXPRESSION";


    public void start(BundleContext context) throws Exception {

        Class<UnlockContentTimer> clazz = com.dotcms.job.unlockcontent.UnlockContentTimer.class;
        String cron = OSGiPluginProperties.getProperty(CRON_EXPRESSION);
        
        // Initializing services...
        initializeServices(context);
        publishBundleServices(context);
        
        Map<String, Object> params = new HashMap<String, Object>();


        // give us a minute before we fire the first time
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 0);


        // Creating our custom Quartz Job
        CronScheduledTask cronScheduledTask = new CronScheduledTask(JOB_NAME, JOB_GROUP, JOB_NAME,
                        clazz.getName(), cal.getTime(), null,
                        CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW, params, cron);


        QuartzUtils.removeJob(JOB_NAME, JOB_GROUP);




        scheduleQuartzJob(cronScheduledTask);



    }

    public void stop(BundleContext context) throws Exception {
        unregisterServices(context);


    }

}
