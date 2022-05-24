package org.matsim.contrib.osm.examples;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import java.util.HashMap;
import java.util.Map;


public class PopulationDemandGeneration implements Runnable {

    private Map<String, Coord> zoneGeometries = new HashMap<>();

    private CoordinateTransformation ct = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, TransformationFactory.WGS84_UTM29N);

    private Scenario scenario;

    private Population population;

    public static void main(String[] args) {
        PopulationDemandGeneration potsdamPop = new PopulationDemandGeneration();
        potsdamPop.run();
    }

    @Override
    public void run() {
        scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        population = scenario.getPopulation();
        fillZoneData();
        generatePopulation();
        PopulationWriter populationWriter = new PopulationWriter(scenario.getPopulation(), scenario.getNetwork());
        populationWriter.write("plans/population7.xml");
    }

    private void fillZoneData() {
        // Add the locations you want to use here.
        // (with proper coordinates)
        zoneGeometries.put("home1", new Coord((double) 28.507634, (double) -16.310437));
        zoneGeometries.put("work1", new Coord((double) 28.306376, (double) -16.386026));
    }

    private void generatePopulation() {
        generateHomeWorkHomeTrips("home1", "work1", 1); // create 20 trips from zone 'home1' to 'work1'
        //... generate more trips here
    }

    private void generateHomeWorkHomeTrips(String from, String to, int quantity) {
        for (int i=0; i<quantity; ++i) {
            Coord source = zoneGeometries.get(from);
            Coord sink = zoneGeometries.get(to);
            Person person = population.getFactory().createPerson(createId(from, to, i, TransportMode.car));
            Plan plan = population.getFactory().createPlan();
            Coord homeLocation = shoot(ct.transform(source));
            Coord workLocation = shoot(ct.transform(sink));
            plan.addActivity(createHome(homeLocation));
            plan.addLeg(createDriveLeg());
            plan.addActivity(createWork(workLocation));
            plan.addLeg(createDriveLeg());
            plan.addActivity(createHome(homeLocation));
            person.addPlan(plan);
            population.addPerson(person);
        }
    }

    private Leg createDriveLeg() {
        Leg leg = population.getFactory().createLeg(TransportMode.car);
        return leg;
    }

    private Coord shoot(Coord source) {
        // Insert code here to blur the input coordinate.
        // For example, add a random number to the x and y coordinates.
        return source;
    }

    private Activity createWork(Coord workLocation) {
        Activity activity = population.getFactory().createActivityFromCoord("work", workLocation);
        activity.setEndTime(17*60*60);
        return activity;
    }

    private Activity createHome(Coord homeLocation) {
        Activity activity = population.getFactory().createActivityFromCoord("home", homeLocation);
        activity.setEndTime(9*60*60);
        return activity;
    }

    private Id<Person> createId(String source, String sink, int i, String transportMode) {
        return Id.create(transportMode + "_" + source + "_" + sink + "_" + i, Person.class);
    }

}


