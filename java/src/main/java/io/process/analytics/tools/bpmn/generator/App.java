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

import io.process.analytics.tools.bpmn.generator.BPMNLayoutGenerator.ExportType;
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
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<definitions xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:ns0=\"http://camunda.org/schema/zeebe/1.0\" id=\"definitions_c6dec7e7-a2fc-4d16-965f-09549bfcf53d\" targetNamespace=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\">\n" +
                "  <process id=\"work-flow-id\" isExecutable=\"true\" name=\"合同审批\">\n" +
                "    <startEvent id=\"startEvent_626f33f7-3660-4a00-bb2f-1a26aa5862b6\">\n" +
                "      <outgoing>sequenceFlow_d9ce3f8d-1f6b-4326-b666-5bdf3757bd84</outgoing>\n" +
                "    </startEvent>\n" +
                "    <serviceTask id=\"serviceTask_edf22053-7518-4437-a50a-89af8859e074\" name=\"审核人1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abc\"/>\n" +
                "        <ns0:taskHeaders>\n" +
                "          <ns0:header key=\"a\" value=\"b\"/>\n" +
                "          <ns0:header key=\"e\" value=\"d\"/>\n" +
                "        </ns0:taskHeaders>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_d9ce3f8d-1f6b-4326-b666-5bdf3757bd84</incoming>\n" +
                "      <outgoing>sequenceFlow_4197e21a-327b-4788-a884-a60d6e0ec507</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_d9ce3f8d-1f6b-4326-b666-5bdf3757bd84\" sourceRef=\"startEvent_626f33f7-3660-4a00-bb2f-1a26aa5862b6\" targetRef=\"serviceTask_edf22053-7518-4437-a50a-89af8859e074\"/>\n" +
                "    <exclusiveGateway id=\"exclusiveGateway_b0d548c9-e6ff-4305-abd4-e468029f8133\" name=\"排他\">\n" +
                "      <incoming>sequenceFlow_4197e21a-327b-4788-a884-a60d6e0ec507</incoming>\n" +
                "      <outgoing>sequenceFlow_a999c022-9fbd-4777-a7ba-c08fa7c7d9bb</outgoing>\n" +
                "      <outgoing>sequenceFlow_33af244d-67d6-4b72-9409-ee362f996ebd</outgoing>\n" +
                "    </exclusiveGateway>\n" +
                "    <sequenceFlow id=\"sequenceFlow_4197e21a-327b-4788-a884-a60d6e0ec507\" sourceRef=\"serviceTask_edf22053-7518-4437-a50a-89af8859e074\" targetRef=\"exclusiveGateway_b0d548c9-e6ff-4305-abd4-e468029f8133\"/>\n" +
                "    <serviceTask id=\"serviceTask_531edc55-756c-455f-82f7-b368d388ad08\" name=\"审核人2.1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abd\"/>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_a999c022-9fbd-4777-a7ba-c08fa7c7d9bb</incoming>\n" +
                "      <outgoing>sequenceFlow_6554575e-729a-473b-8600-a89f465d6353</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_a999c022-9fbd-4777-a7ba-c08fa7c7d9bb\" name=\"条件1\" sourceRef=\"exclusiveGateway_b0d548c9-e6ff-4305-abd4-e468029f8133\" targetRef=\"serviceTask_531edc55-756c-455f-82f7-b368d388ad08\">\n" +
                "      <conditionExpression id=\"conditionExpression_e299f97c-a953-40ce-bb2f-083170e1d421\">=id&gt;1</conditionExpression>\n" +
                "    </sequenceFlow>\n" +
                "    <serviceTask id=\"serviceTask_3f692a5b-39ee-4d5a-a5ff-4bee0b7cbc26\" name=\"审核人2.1.1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abd.1\"/>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_6554575e-729a-473b-8600-a89f465d6353</incoming>\n" +
                "      <outgoing>sequenceFlow_245df99b-6197-46da-bce2-fe2d7be9028d</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_6554575e-729a-473b-8600-a89f465d6353\" sourceRef=\"serviceTask_531edc55-756c-455f-82f7-b368d388ad08\" targetRef=\"serviceTask_3f692a5b-39ee-4d5a-a5ff-4bee0b7cbc26\"/>\n" +
                "    <serviceTask id=\"serviceTask_07ec6916-9ac6-4143-8216-19c976ce1abd\" name=\"审核人2.1.1.1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abd.1.1\"/>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_245df99b-6197-46da-bce2-fe2d7be9028d</incoming>\n" +
                "      <outgoing>sequenceFlow_803eca37-0b0a-477b-9cfa-22716df174c9</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_245df99b-6197-46da-bce2-fe2d7be9028d\" sourceRef=\"serviceTask_3f692a5b-39ee-4d5a-a5ff-4bee0b7cbc26\" targetRef=\"serviceTask_07ec6916-9ac6-4143-8216-19c976ce1abd\"/>\n" +
                "    <exclusiveGateway id=\"exclusiveGateway_cc26303c-2cb8-4f75-947e-836efd5d7888\" name=\"并行网关\">\n" +
                "      <incoming>sequenceFlow_803eca37-0b0a-477b-9cfa-22716df174c9</incoming>\n" +
                "      <incoming>sequenceFlow_33af244d-67d6-4b72-9409-ee362f996ebd</incoming>\n" +
                "      <outgoing>sequenceFlow_d4d68fd3-0ced-4d2f-b71f-4e76ef7976ba</outgoing>\n" +
                "    </exclusiveGateway>\n" +
                "    <sequenceFlow id=\"sequenceFlow_803eca37-0b0a-477b-9cfa-22716df174c9\" sourceRef=\"serviceTask_07ec6916-9ac6-4143-8216-19c976ce1abd\" targetRef=\"exclusiveGateway_cc26303c-2cb8-4f75-947e-836efd5d7888\"/>\n" +
                "    <sequenceFlow id=\"sequenceFlow_33af244d-67d6-4b72-9409-ee362f996ebd\" name=\"条件2\" sourceRef=\"exclusiveGateway_b0d548c9-e6ff-4305-abd4-e468029f8133\" targetRef=\"exclusiveGateway_cc26303c-2cb8-4f75-947e-836efd5d7888\">\n" +
                "      <conditionExpression id=\"conditionExpression_1c9ab5d8-835f-4c6b-b2d1-37a7cf6d4934\">=id&lt;1</conditionExpression>\n" +
                "    </sequenceFlow>\n" +
                "    <serviceTask id=\"serviceTask_d37fed63-bd9d-42d4-b6d9-98abf6caed77\" name=\"审核人3.1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abd.3.1\"/>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_d4d68fd3-0ced-4d2f-b71f-4e76ef7976ba</incoming>\n" +
                "      <outgoing>sequenceFlow_77f14605-2349-4f1e-950c-7f8ef194e624</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_d4d68fd3-0ced-4d2f-b71f-4e76ef7976ba\" sourceRef=\"exclusiveGateway_cc26303c-2cb8-4f75-947e-836efd5d7888\" targetRef=\"serviceTask_d37fed63-bd9d-42d4-b6d9-98abf6caed77\"/>\n" +
                "    <endEvent id=\"endEvent_6644f886-4df4-4210-8d77-677630f4d131\">\n" +
                "      <incoming>sequenceFlow_77f14605-2349-4f1e-950c-7f8ef194e624</incoming>\n" +
                "    </endEvent>\n" +
                "    <sequenceFlow id=\"sequenceFlow_77f14605-2349-4f1e-950c-7f8ef194e624\" sourceRef=\"serviceTask_d37fed63-bd9d-42d4-b6d9-98abf6caed77\" targetRef=\"endEvent_6644f886-4df4-4210-8d77-677630f4d131\"/>\n" +
                "  </process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_c82c2220-026d-4106-b54a-c45e7317c725\">\n" +
                "    <bpmndi:BPMNPlane bpmnElement=\"work-flow-id\" id=\"BPMNPlane_51ab2346-a110-4226-8b7e-6d58c742b444\">\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"startEvent_626f33f7-3660-4a00-bb2f-1a26aa5862b6\" id=\"BPMNShape_6ebd2ac8-6f42-45c2-971b-16f1f304664e\">\n" +
                "        <dc:Bounds height=\"36.0\" width=\"36.0\" x=\"100.0\" y=\"100.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_edf22053-7518-4437-a50a-89af8859e074\" id=\"BPMNShape_3f8c4841-33ed-4b55-b233-78a6a84b248d\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"186.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_d9ce3f8d-1f6b-4326-b666-5bdf3757bd84\" id=\"BPMNEdge_70db88af-236c-46a4-9cc6-9a8c4b730e88\">\n" +
                "        <di:waypoint x=\"136.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"186.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"exclusiveGateway_b0d548c9-e6ff-4305-abd4-e468029f8133\" id=\"BPMNShape_72643d34-06b9-4615-89be-275907bedd33\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds height=\"50.0\" width=\"50.0\" x=\"336.0\" y=\"93.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_4197e21a-327b-4788-a884-a60d6e0ec507\" id=\"BPMNEdge_fd97a7ab-56af-4a0d-b3eb-1ad8f16e7d5f\">\n" +
                "        <di:waypoint x=\"286.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"336.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_531edc55-756c-455f-82f7-b368d388ad08\" id=\"BPMNShape_018983e5-2cf5-4c3e-ac33-e590a366252b\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"436.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_a999c022-9fbd-4777-a7ba-c08fa7c7d9bb\" id=\"BPMNEdge_1fcef364-f309-427d-b18d-c0a7c835934a\">\n" +
                "        <di:waypoint x=\"386.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"436.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_3f692a5b-39ee-4d5a-a5ff-4bee0b7cbc26\" id=\"BPMNShape_3e4c2355-ec2f-4f8b-9c18-c2faa4e3d86e\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"586.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_6554575e-729a-473b-8600-a89f465d6353\" id=\"BPMNEdge_c8a3abb9-6e10-41be-a2cc-f27d7dbff3b0\">\n" +
                "        <di:waypoint x=\"536.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"586.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_07ec6916-9ac6-4143-8216-19c976ce1abd\" id=\"BPMNShape_5532d12e-67ba-409a-8d5e-58a9698fc459\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"736.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_245df99b-6197-46da-bce2-fe2d7be9028d\" id=\"BPMNEdge_114c300d-eeea-45ed-806c-57e40fad8ba1\">\n" +
                "        <di:waypoint x=\"686.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"736.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"exclusiveGateway_cc26303c-2cb8-4f75-947e-836efd5d7888\" id=\"BPMNShape_bd8fc78c-d1dc-4bbb-b212-6aa64ac323cf\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds height=\"50.0\" width=\"50.0\" x=\"886.0\" y=\"93.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_803eca37-0b0a-477b-9cfa-22716df174c9\" id=\"BPMNEdge_0d9d5eb5-62f0-42ea-9b5d-26cbe881a9a2\">\n" +
                "        <di:waypoint x=\"836.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"886.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_33af244d-67d6-4b72-9409-ee362f996ebd\" id=\"BPMNEdge_acb92a4d-02b1-46bb-89fb-0c8706e7b7e3\">\n" +
                "        <di:waypoint x=\"361.0\" y=\"143.0\"/>\n" +
                "        <di:waypoint x=\"361.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"886.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_d37fed63-bd9d-42d4-b6d9-98abf6caed77\" id=\"BPMNShape_9a4e584f-486b-4f75-9ab2-88eeb0994877\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"986.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_d4d68fd3-0ced-4d2f-b71f-4e76ef7976ba\" id=\"BPMNEdge_21f0b09c-e659-4ded-9cc5-0d75b735c40a\">\n" +
                "        <di:waypoint x=\"936.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"986.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"endEvent_6644f886-4df4-4210-8d77-677630f4d131\" id=\"BPMNShape_001a09e0-bdae-48ce-8452-705fadc419d0\">\n" +
                "        <dc:Bounds height=\"36.0\" width=\"36.0\" x=\"1136.0\" y=\"100.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_77f14605-2349-4f1e-950c-7f8ef194e624\" id=\"BPMNEdge_75a54b18-aa34-4b6f-bf86-6e0784dca4c1\">\n" +
                "        <di:waypoint x=\"1086.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"1136.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</definitions>";
//        int exitCode = runApp(args);
//        System.exit(exitCode);

        BPMNLayoutGenerator bpmnLayoutGenerator = new BPMNLayoutGenerator();
        String output = bpmnLayoutGenerator.generateLayoutFromBPMNSemantic(s, exportType("BPMN"));
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
            BPMNLayoutGenerator bpmnLayoutGenerator = new BPMNLayoutGenerator();
            String output;
            switch (inputType) {
                case "BPMN":
                    if (inputFiles.length != 1) {
                        System.err.println("Expected only one input file to import from BPMN format, got: " + inputType.length());
                    }
                    output = bpmnLayoutGenerator.generateLayoutFromBPMNSemantic(FileUtils.fileContent(inputFiles[0]), exportType(outputType));
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


    private static ExportType exportType(String arg) {
        try {
            return ExportType.valueOf(arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    format("Invalid export type: %s. Must be one of [%s]", arg,
                            stream(ExportType.values())
                                    .map(Enum::toString)
                                    .map(String::toLowerCase)
                                    .collect(joining(", "))));
        }
    }

}
