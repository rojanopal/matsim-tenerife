package org.matsim.contrib.osm.examples;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.osm.networkReader.SupersonicOsmNetworkReader;
import org.matsim.core.network.algorithms.NetworkCleaner;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;

public class MyNetworkReader {

    private static CoordinateTransformation coordinateTransformation = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, "EPSG:25832");
    private static String inputFile = "maps/map_tenerife_finalversion1.pbf";
    private static String outputFile = "maps/myNetwork.xml";
    public static void main(String[] args) {


        Network network = new SupersonicOsmNetworkReader.Builder().setCoordinateTransformation(coordinateTransformation)
                .build()
                .read(inputFile);

        NetworkWriter networkWriter = new NetworkWriter(network);

        new NetworkCleaner().run(network);

        networkWriter.write(outputFile);
    }
}