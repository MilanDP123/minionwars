package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.Effects.*;
import be.ugent.objprog.minionwars.Fields.*;
import be.ugent.objprog.minionwars.Minions.MinionButton;
import be.ugent.objprog.minionwars.Powers.PowerButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class XMLReader {

    private final String path;
    private final MinionWarsModel model;

    private final Map<String, FieldFactory> fieldFactories = Map.of(
            "dirt", new DirtFactory(),
            "forest", new ForestFactory(),
            "mountains", new MountainsFactory(),
            "water", new WaterFactory()
    );

    public XMLReader(String path, MinionWarsModel model) {
        this.path = Paths.get(path).toAbsolutePath().toString();
        this.model = model;
    }

    public List<Element> getFields() throws IOException, JDOMException {
        Document document = new SAXBuilder().build(new FileInputStream(path));
        Element root = document.getRootElement();
        return root.getChild("field").getChildren();
    }

    public void makeFields(List<Element> fields, AnchorPane pane) {
        //TODO make minionButton with getters en setters
        Map<String, Overlay> overlays = new HashMap<>();
        for (Element element : fields) {
            int x = Integer.parseInt(element.getAttributeValue("x"));
            int y = Integer.parseInt(element.getAttributeValue("y"));
            Overlay overlay = new Overlay(x, y);
            overlays.put(x + " " + y, overlay);
        }
        int maxX = 0;
        int maxY = 0;
        ArrayList<Field> fieldArrayList = new ArrayList<>();
        for (Element element : fields) {
            String name = element.getName();
            int x = Integer.parseInt(element.getAttributeValue("x"));
            int y = Integer.parseInt(element.getAttributeValue("y"));
            String homeBaseString = element.getAttributeValue("homebase");
            int homeBase = homeBaseString != null ? Integer.parseInt(homeBaseString) : 0;
            Overlay overlay = overlays.get(x + " " + y);
            Field field = fieldFactories.get(name).createField(x, y, model, homeBase, overlay);
            if (field.getX() > maxX) {
                maxX = field.getX();
            }
            if (field.getY() > maxY) {
                maxY = field.getY();
            }
            overlay.setField(field);
            pane.getChildren().add(field);
            field.setImage();
            fieldArrayList.add(field);
        }
        model.setFields(fieldArrayList, maxX + 1, maxY + 1);
        overlays.forEach((loc, overlay) -> pane.getChildren().add(overlay));
    }

    public List<Element> getMinions() throws IOException, JDOMException {
        Document document = new SAXBuilder().build(new FileInputStream(path));
        Element root = document.getRootElement();
        return root.getChild("minions").getChildren();
    }

    public void makeMinionButtons(List<Element> minions, VBox pane) {
        //TODO make minionButton with getters en setters
        for (Element minion: minions) {
            String name = minion.getAttributeValue("name");
            String type = minion.getName();
            int cost = Integer.parseInt(minion.getAttributeValue("cost"));
            int movement = Integer.parseInt(minion.getAttributeValue("movement"));
            int attack = Integer.parseInt(minion.getAttributeValue("attack"));
            int defence = Integer.parseInt(minion.getAttributeValue("defence"));
            String[] splitedRange = minion.getAttributeValue("range").split("\\s+");
            int minRange = Integer.parseInt(splitedRange[0]);
            int maxRange = Integer.parseInt(splitedRange[1]);

            String effectType = minion.getAttributeValue("effect");

            String effectValueString = minion.getAttributeValue("effect-value");
            Integer effectValue =effectValueString != null ? Integer.parseInt(effectValueString) : null;

            MinionButton minionButton = new MinionButton(name, type, cost, movement, attack, defence, minRange, maxRange, effectType, effectValue, model);
            pane.getChildren().add(minionButton);
        }
    }

    public List<Element> getEffectElements() throws IOException, JDOMException {
        Document document = new SAXBuilder().build(new FileInputStream(path));
        Element root = document.getRootElement();
        return root.getChild("effects").getChildren();
    }

    public Map<String, EffectFactory> getEffectFactoriesMap(List<Element> effects) {
        Map<String, Supplier<EffectFactory>> factories = Map.of(
                "blindness", BlindnessFactory::new,
                "burn", BurnFactory::new,
                "heal", HealFactory::new,
                "paralysis", ParalysisFactory::new,
                "poison", PoisonFactory::new,
                "rage", RageFactory::new,
                "slow", SlowFactory::new
        );

        Map<String, EffectFactory> effectFactoryMap = new HashMap<>();
        for (Element element : effects) {
            EffectFactory factory = factories.get(element.getName()).get();
            factory.setName(element.getAttributeValue("name"));
            factory.setDuration(Integer.parseInt(element.getAttributeValue("duration")));
            factory.setValue(Integer.parseInt(element.getAttributeValue("value") != null ? element.getAttributeValue("value") : "0"));
            effectFactoryMap.put(element.getName(), factory);
        }
        return effectFactoryMap;
    }

    public List<Element> getPowerElements() throws IOException, JDOMException {
        Document document = new SAXBuilder().build(new FileInputStream(path));
        Element root = document.getRootElement();
        return root.getChild("powers").getChildren();
    }

    public void addPowerButtons(List<Element> powers, Map<String, EffectFactory> effectFactoryMap,VBox pane) {
        for (Element power: powers) {
            PowerButton powerButton = new PowerButton();
            powerButton.setModel(model);
            powerButton.setType(power.getName());
            powerButton.setName(power.getAttributeValue("name"));

            powerButton.setValue(Integer.parseInt(power.getAttributeValue("value") != null ? power.getAttributeValue("value") : "0"));
            powerButton.setRadius(Integer.parseInt(power.getAttributeValue("radius")));
            String effectValue = power.getAttributeValue("effect-value");
            if (power.getAttributeValue("effect") != null) {
                if (effectValue != null) {
                    powerButton.setEffect(effectFactoryMap.get(power.getAttributeValue("effect")).createEffect(Integer.parseInt(effectValue)));
                } else {
                    powerButton.setEffect(effectFactoryMap.get(power.getAttributeValue("effect")).createEffect());
                }
            }
            pane.getChildren().add(powerButton);
        }
    }
}
