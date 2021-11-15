/*
 * Copyright 2020 Bonitasoft S.A.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.process.analytics.tools.bpmn.generator;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import io.process.analytics.tools.bpmn.generator.internal.FileUtils;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Log4j2
@Command(name = "App", mixinStandardHelpOptions = true)
public class App implements Callable<Integer> {


    @Option(names = {"-t", "--input-type"},
            description = "BPMN or CSV.",
            paramLabel = "TYPE")
    String inputType = "BPMN";
    @Option(names = {"-u", "--output-type"},
            description = "BPMN, SVG or ASCII.",
            paramLabel = "TYPE")
    String outputType = "BPMN";

    @Option(names = {"-o", "--output"},
            description = "Output file.",
            paramLabel = "OUTPUT")
    private File outputFile;
    @Parameters(arity = "1..2", paramLabel = "INPUT", description = "Input file(s).")
    private File[] inputFiles;

    public static void main(String[] args) throws Exception {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:zeebe=\"http://camunda.org/schema/zeebe/1.0\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:modeler=\"http://camunda.org/schema/modeler/1.0\" id=\"Definitions_1ijswu3\" targetNamespace=\"http://bpmn.io/schema/bpmn\" exporter=\"Camunda Modeler\" exporterVersion=\"4.9.0\" modeler:executionPlatform=\"Camunda Cloud\" modeler:executionPlatformVersion=\"1.0.0\">\n" +
                "  <bpmn:process id=\"test-pull-job\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\">\n" +
                "      <bpmn:outgoing>Flow_0dnklzn</bpmn:outgoing>\n" +
                "    </bpmn:startEvent>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0dnklzn\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_0shpzab\" />\n" +
                "    <bpmn:serviceTask id=\"Activity_0shpzab\">\n" +
                "      <bpmn:extensionElements>\n" +
                "        <zeebe:taskDefinition type=\"test\" />\n" +
                "      </bpmn:extensionElements>\n" +
                "      <bpmn:incoming>Flow_0dnklzn</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_150nvhv</bpmn:outgoing>\n" +
                "    </bpmn:serviceTask>\n" +
                "    <bpmn:exclusiveGateway id=\"Gateway_1g86g2i\">\n" +
                "      <bpmn:incoming>Flow_150nvhv</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_124pdo3</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_1oe40ye</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_0m1nllj</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_150nvhv\" sourceRef=\"Activity_0shpzab\" targetRef=\"Gateway_1g86g2i\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_124pdo3\" sourceRef=\"Gateway_1g86g2i\" targetRef=\"Activity_0jl7dcs\" />\n" +
                "    <bpmn:exclusiveGateway id=\"Gateway_05r54hi\">\n" +
                "      <bpmn:incoming>Flow_1oe40ye</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1fhu01x</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_0dtlfcp</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1oe40ye\" sourceRef=\"Gateway_1g86g2i\" targetRef=\"Gateway_05r54hi\" />\n" +
                "    <bpmn:task id=\"Activity_1rsbezp\">\n" +
                "      <bpmn:incoming>Flow_0m1nllj</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_0dijbgr</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0m1nllj\" sourceRef=\"Gateway_1g86g2i\" targetRef=\"Activity_1rsbezp\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1fhu01x\" sourceRef=\"Gateway_05r54hi\" targetRef=\"Activity_0rajcz9\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0dtlfcp\" sourceRef=\"Gateway_05r54hi\" targetRef=\"Activity_12tbhrt\" />\n" +
                "    <bpmn:exclusiveGateway id=\"Gateway_0k27m6b\">\n" +
                "      <bpmn:incoming>Flow_13ezf00</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_1whr29b</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_061vyem</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_13ezf00\" sourceRef=\"Activity_0rajcz9\" targetRef=\"Gateway_0k27m6b\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1whr29b\" sourceRef=\"Activity_12tbhrt\" targetRef=\"Gateway_0k27m6b\" />\n" +
                "    <bpmn:exclusiveGateway id=\"Gateway_1vf2gi7\">\n" +
                "      <bpmn:incoming>Flow_18nkndt</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_0dijbgr</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_061vyem</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_11w0k5m</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_18nkndt\" sourceRef=\"Activity_0jl7dcs\" targetRef=\"Gateway_1vf2gi7\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0dijbgr\" sourceRef=\"Activity_1rsbezp\" targetRef=\"Gateway_1vf2gi7\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_061vyem\" sourceRef=\"Gateway_0k27m6b\" targetRef=\"Gateway_1vf2gi7\" />\n" +
                "    <bpmn:endEvent id=\"Event_017f1io\">\n" +
                "      <bpmn:incoming>Flow_11w0k5m</bpmn:incoming>\n" +
                "    </bpmn:endEvent>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_11w0k5m\" sourceRef=\"Gateway_1vf2gi7\" targetRef=\"Event_017f1io\" />\n" +
                "    <bpmn:serviceTask id=\"Activity_0rajcz9\">\n" +
                "      <bpmn:incoming>Flow_1fhu01x</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_13ezf00</bpmn:outgoing>\n" +
                "    </bpmn:serviceTask>\n" +
                "    <bpmn:serviceTask id=\"Activity_0jl7dcs\">\n" +
                "      <bpmn:incoming>Flow_124pdo3</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_18nkndt</bpmn:outgoing>\n" +
                "    </bpmn:serviceTask>\n" +
                "    <bpmn:serviceTask id=\"Activity_12tbhrt\">\n" +
                "      <bpmn:incoming>Flow_0dtlfcp</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1whr29b</bpmn:outgoing>\n" +
                "    </bpmn:serviceTask>\n" +
                "  </bpmn:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"test-pull-job\">\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0dnklzn_di\" bpmnElement=\"Flow_0dnklzn\">\n" +
                "        <di:waypoint x=\"215\" y=\"117\" />\n" +
                "        <di:waypoint x=\"290\" y=\"117\" />\n" +
                "        <di:waypoint x=\"290\" y=\"160\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_150nvhv_di\" bpmnElement=\"Flow_150nvhv\">\n" +
                "        <di:waypoint x=\"340\" y=\"200\" />\n" +
                "        <di:waypoint x=\"430\" y=\"200\" />\n" +
                "        <di:waypoint x=\"430\" y=\"142\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_124pdo3_di\" bpmnElement=\"Flow_124pdo3\">\n" +
                "        <di:waypoint x=\"455\" y=\"117\" />\n" +
                "        <di:waypoint x=\"520\" y=\"117\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1oe40ye_di\" bpmnElement=\"Flow_1oe40ye\">\n" +
                "        <di:waypoint x=\"430\" y=\"142\" />\n" +
                "        <di:waypoint x=\"430\" y=\"230\" />\n" +
                "        <di:waypoint x=\"525\" y=\"230\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0m1nllj_di\" bpmnElement=\"Flow_0m1nllj\">\n" +
                "        <di:waypoint x=\"430\" y=\"142\" />\n" +
                "        <di:waypoint x=\"430\" y=\"550\" />\n" +
                "        <di:waypoint x=\"780\" y=\"550\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1fhu01x_di\" bpmnElement=\"Flow_1fhu01x\">\n" +
                "        <di:waypoint x=\"575\" y=\"230\" />\n" +
                "        <di:waypoint x=\"650\" y=\"230\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0dtlfcp_di\" bpmnElement=\"Flow_0dtlfcp\">\n" +
                "        <di:waypoint x=\"550\" y=\"255\" />\n" +
                "        <di:waypoint x=\"550\" y=\"340\" />\n" +
                "        <di:waypoint x=\"650\" y=\"340\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_13ezf00_di\" bpmnElement=\"Flow_13ezf00\">\n" +
                "        <di:waypoint x=\"750\" y=\"230\" />\n" +
                "        <di:waypoint x=\"825\" y=\"230\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1whr29b_di\" bpmnElement=\"Flow_1whr29b\">\n" +
                "        <di:waypoint x=\"750\" y=\"340\" />\n" +
                "        <di:waypoint x=\"850\" y=\"340\" />\n" +
                "        <di:waypoint x=\"850\" y=\"255\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_18nkndt_di\" bpmnElement=\"Flow_18nkndt\">\n" +
                "        <di:waypoint x=\"620\" y=\"117\" />\n" +
                "        <di:waypoint x=\"965\" y=\"117\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0dijbgr_di\" bpmnElement=\"Flow_0dijbgr\">\n" +
                "        <di:waypoint x=\"880\" y=\"550\" />\n" +
                "        <di:waypoint x=\"990\" y=\"550\" />\n" +
                "        <di:waypoint x=\"990\" y=\"142\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_061vyem_di\" bpmnElement=\"Flow_061vyem\">\n" +
                "        <di:waypoint x=\"850\" y=\"205\" />\n" +
                "        <di:waypoint x=\"850\" y=\"117\" />\n" +
                "        <di:waypoint x=\"965\" y=\"117\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_11w0k5m_di\" bpmnElement=\"Flow_11w0k5m\">\n" +
                "        <di:waypoint x=\"1015\" y=\"117\" />\n" +
                "        <di:waypoint x=\"1102\" y=\"117\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"179\" y=\"99\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_19p5mu7_di\" bpmnElement=\"Activity_0shpzab\">\n" +
                "        <dc:Bounds x=\"240\" y=\"160\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Gateway_1g86g2i_di\" bpmnElement=\"Gateway_1g86g2i\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"405\" y=\"92\" width=\"50\" height=\"50\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Gateway_05r54hi_di\" bpmnElement=\"Gateway_05r54hi\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"525\" y=\"205\" width=\"50\" height=\"50\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Gateway_0k27m6b_di\" bpmnElement=\"Gateway_0k27m6b\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"825\" y=\"205\" width=\"50\" height=\"50\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Gateway_1vf2gi7_di\" bpmnElement=\"Gateway_1vf2gi7\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"965\" y=\"92\" width=\"50\" height=\"50\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1rsbezp_di\" bpmnElement=\"Activity_1rsbezp\">\n" +
                "        <dc:Bounds x=\"780\" y=\"510\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_017f1io_di\" bpmnElement=\"Event_017f1io\">\n" +
                "        <dc:Bounds x=\"1102\" y=\"99\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1fqxp92_di\" bpmnElement=\"Activity_0rajcz9\">\n" +
                "        <dc:Bounds x=\"650\" y=\"190\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1sfbwvt_di\" bpmnElement=\"Activity_0jl7dcs\">\n" +
                "        <dc:Bounds x=\"520\" y=\"77\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1fs6d0y_di\" bpmnElement=\"Activity_12tbhrt\">\n" +
                "        <dc:Bounds x=\"650\" y=\"300\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn:definitions>\n";
//        int exitCode = runApp(args);
//        System.exit(exitCode);

        BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout();
        String output = bpmnAutoLayout.generateLayoutFromBPMNSemantic(s, BpmnAutoLayout.ExportType.BPMN);
        System.out.println(output);
    }

    static int runApp(String... args) {
        return new CommandLine(new App()).execute(args);
    }


    private App() {
    }

    @Override
    public Integer call() {
        try {
            BpmnAutoLayout bpmnLayoutGenerator = new BpmnAutoLayout();
            String output;
            switch (inputType) {
                case "BPMN":
                    if (inputFiles.length != 1) {
                        System.err.println("Expected only one input file to import from BPMN format, got: " + inputType.length());
                    }
                    output = bpmnLayoutGenerator.generateLayoutFromBPMNSemantic(FileUtils.fileContent(inputFiles[0]), BpmnAutoLayout.ExportType.BPMN);
                    break;
                case "CSV":
                    if (inputFiles.length != 2) {
                        System.err.println("Expected 2 input files to import from CSV format, got: " + inputType.length());
                    }
                    output = bpmnLayoutGenerator.generateLayoutFromCSV(FileUtils.fileContent(inputFiles[0]), FileUtils.fileContent(inputFiles[1]), exportType(outputType));
                    break;
                default:
                    System.err.println("Unexpected input type: " + inputType);
                    return 2;
            }
            if (outputFile != null) {
                FileUtils.touch(outputFile);
                Files.write(outputFile.toPath(), output.getBytes());
            } else {
                System.out.println(output);
            }
        } catch (NoSuchFileException e) {
            System.err.println("File not found: " + e.getMessage());
            return 2;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred: " + e.getMessage());
            return 1;
        }
        return 0;
    }


    private static BpmnAutoLayout.ExportType exportType(String arg) {
        try {
            return BpmnAutoLayout.ExportType.valueOf(arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    format("Invalid export type: %s. Must be one of [%s]", arg,
                            stream(BpmnAutoLayout.ExportType.values())
                                    .map(Enum::toString)
                                    .map(String::toLowerCase)
                                    .collect(joining(", "))));
        }
    }

}
