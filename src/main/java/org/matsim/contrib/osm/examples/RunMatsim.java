package org.matsim.contrib.osm.examples;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;


import java.util.Date;

public class RunMatsim {

    public static void main(String[] args) {
        Config config = ConfigUtils.createConfig();
        config.controler().setOutputDirectory("output"+ (new Date()).getTime());
        config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );
        config.controler().setLastIteration(0);
        config.network().setInputFile("scenarios/tenerife1/myNetwork.xml");
        config.plans().setInputFile("scenarios/tenerife1/population.xml");

        PlanCalcScoreConfigGroup.ActivityParams paramsWork = new PlanCalcScoreConfigGroup.ActivityParams("work");
        paramsWork.setTypicalDuration(8*3600);
        config.planCalcScore().addActivityParams(paramsWork);
        PlanCalcScoreConfigGroup.ActivityParams paramsHome = new PlanCalcScoreConfigGroup.ActivityParams("home");
        paramsHome.setTypicalDuration(8*3600);
        config.planCalcScore().addActivityParams(paramsHome);


        // possibly modify config here

        // ---

        Scenario scenario = ScenarioUtils.loadScenario(config) ;


        // possibly modify scenario here

        // ---

        Controler controler = new Controler( scenario ) ;

        // possibly modify controler here

//		controler.addOverridingModule( new OTFVisLiveModule() ) ;
        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                this.addEventHandlerBinding().to(MyHandler.class);
            }
        });


        // ---

        controler.run();
    }

    private static class MyHandler implements LinkEnterEventHandler {

        @Override
        public void handleEvent(LinkEnterEvent linkEnterEvent) {
//            System.err.println("Vehicle "+ linkEnterEvent.getVehicleId()+ " enters link " + linkEnterEvent.getLinkId() + " at "+ linkEnterEvent.getTime() );
        }
    }

}
