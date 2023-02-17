package mixedreality.lab.exercise7;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import mixedreality.base.mesh.TriangleMesh;
import mixedreality.base.mesh.TriangleMeshTools;
import mixedreality.lab.exercise7.functions.GourSat;
import mixedreality.lab.exercise7.functions.ImplicitFunction;
import mixedreality.lab.exercise7.functions.Sphere;
import mixedreality.lab.exercise7.functions.Torus;
import mixedreality.lab.solution.exercise7.MarchingCubesSolution;
import ui.AbstractCameraController;
import ui.Scene3D;

public class MarchingCubesScene3D extends Scene3D {

    private MarchingCubes mc;

    private ImplicitFunction f;

    private int res = 40;

    private AssetManager assetManager;
    private Node rootNode;
    private AbstractCameraController cameraController;

    public MarchingCubesScene3D() {
        this.f = new Sphere(0.5f, new Vector3f(0, 0, 0));
        this.mc = new MarchingCubesSolution();

        System.out.println("Functions: ");
        System.out.println(" 1: Sphere");
        System.out.println(" 2: Goursat Shape");
        System.out.println(" 2: Torus");
    }

    @Override
    public void init(AssetManager assetManager, Node rootNode, AbstractCameraController cameraController) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.cameraController = cameraController;
        rebuildScene();
    }

    @Override
    public void update(float time) {
    }

    @Override
    public void render() {
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void handleKey(String keyId) {
        if (keyId.equals("KEY_1")) {
            f = new Sphere(0.5f, new Vector3f(0, 0, 0));
            rebuildScene();
        } else if (keyId.equals("KEY_2")) {
            f = new GourSat();
            rebuildScene();
        }else if (keyId.equals("KEY_3")) {
            f = new Torus(0.5f, 0.25f);
            rebuildScene();
        }
    }

    private void rebuildScene() {
        rootNode.detachAllChildren();

        TriangleMesh mesh = mc.makeMesh(f, 0,
                new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1),
                res, res, res);
        mesh.setColor(ColorRGBA.Orange);

        Geometry node = TriangleMeshTools.createJMonkeyMesh(assetManager, mesh);
        rootNode.attachChild(node);

        cameraController.adjustViewTo(mesh.getBoundingBox());
    }

    @Override
    public void setupLights(Node rootNode, ViewPort viewPort) {
        // Clear lights
        for (Light light : rootNode.getLocalLightList()) {
            rootNode.removeLight(light);
        }
        for (Light light : rootNode.getWorldLightList()) {
            rootNode.removeLight(light);
        }

        // Sun
        PointLight sun = new PointLight();
        sun.setPosition(new Vector3f(3,3,3));
        sun.setColor(ColorRGBA.White);
        //sun.setDirection(new Vector3f(0.25f, -1, 0.1f));
        rootNode.addLight(sun);

        AmbientLight ambientLight = new AmbientLight();
        ColorRGBA darkAmbientColor = ColorRGBA.DarkGray;
        ambientLight.setColor(darkAmbientColor);
        rootNode.addLight(ambientLight);
    }
}
