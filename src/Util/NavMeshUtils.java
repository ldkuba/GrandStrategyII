package Util;

import Util.NavMeshGenerator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jme3tools.optimize.GeometryBatchFactory;

import com.jme3.ai.navmesh.NavMesh;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class NavMeshUtils {

	private AssetManager assetManager;
	
	public NavMeshUtils(AssetManager assetManager){
		this.assetManager = assetManager;
	}
	
//	public NavMesh buildNavMesh(Node node){
//		Mesh mesh = new Mesh();
//		NavMesh navMesh = new NavMesh();
//		NavMeshGenerator generator = new NavMeshGenerator();
//		generator.setCellHeight(1f);
//
//		GeometryBatchFactory.mergeGeometries(findGeometries(node, new LinkedList<Geometry>()), mesh);
//		Mesh optiMesh = generator.optimize(mesh);
//
//		navMesh.loadFromMesh(optiMesh);
//		return navMesh;
//	}
	
	public Mesh buildMeshForNavMesh(Node node){
		
		Mesh mesh = new Mesh();
		NavMeshGenerator generator = new NavMeshGenerator();
		generator.setCellHeight(1f);

		GeometryBatchFactory.mergeGeometries(findGeometries(node, new LinkedList<Geometry>()), mesh);
		Mesh optiMesh = generator.optimize(mesh);

		return optiMesh;
	}
	
	public Geometry createNavMesh(Node node) {
		Mesh mesh = new Mesh();
		NavMesh navMesh = new NavMesh();
		NavMeshGenerator generator = new NavMeshGenerator();
		generator.setCellHeight(1f);

		GeometryBatchFactory.mergeGeometries(findGeometries(node, new LinkedList<Geometry>()), mesh);
		Mesh optiMesh = generator.optimize(mesh);

		navMesh.loadFromMesh(optiMesh);

		Geometry navGeom = new Geometry("NavMesh");
		navGeom.setMesh(optiMesh);
		Material green = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		green.setColor("Color", ColorRGBA.Green);
		//green.getAdditionalRenderState().setWireframe(true);
		navGeom.setMaterial(green);

		return navGeom;
	}
	
	private List<Geometry> findGeometries(Node node, List<Geometry> geoms) {
        for (Iterator<Spatial> it = node.getChildren().iterator(); it.hasNext();) {
            Spatial spatial = it.next();
            if (spatial instanceof Geometry) {
                geoms.add((Geometry) spatial);
            } else if (spatial instanceof Node) {
                findGeometries((Node) spatial, geoms);
            }
        }
        return geoms;
    }
}