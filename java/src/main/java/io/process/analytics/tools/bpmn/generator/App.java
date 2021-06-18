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
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<definitions xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:ns0=\"http://camunda.org/schema/zeebe/1.0\" id=\"definitions_48cac798-d1bf-4e48-8508-938dd8baceec\" targetNamespace=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\">\n" +
                "  <process id=\"work-flow-id\" isExecutable=\"true\" name=\"合同审批\">\n" +
                "    <startEvent id=\"startEvent_999bcc05-d4c1-4607-952a-0c22359201c5\">\n" +
                "      <outgoing>sequenceFlow_813b5f65-553a-4087-8829-067bdcd58d6d</outgoing>\n" +
                "    </startEvent>\n" +
                "    <serviceTask id=\"serviceTask_9839a6b6-0000-433f-8dd7-83420c7dc65a\" name=\"审核人1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abc\"/>\n" +
                "        <ns0:taskHeaders>\n" +
                "          <ns0:header key=\"a\" value=\"b\"/>\n" +
                "          <ns0:header key=\"e\" value=\"d\"/>\n" +
                "        </ns0:taskHeaders>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_813b5f65-553a-4087-8829-067bdcd58d6d</incoming>\n" +
                "      <outgoing>sequenceFlow_b4e12839-3e5a-4d74-a27b-14ef1997aeac</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_813b5f65-553a-4087-8829-067bdcd58d6d\" sourceRef=\"startEvent_999bcc05-d4c1-4607-952a-0c22359201c5\" targetRef=\"serviceTask_9839a6b6-0000-433f-8dd7-83420c7dc65a\"/>\n" +
                "    <exclusiveGateway id=\"exclusiveGateway_31165bb6-71b6-470e-a975-9bf3aee8c4a2\" name=\"排他\">\n" +
                "      <incoming>sequenceFlow_b4e12839-3e5a-4d74-a27b-14ef1997aeac</incoming>\n" +
                "      <outgoing>sequenceFlow_610a845a-9fd2-445d-99a1-9300af328754</outgoing>\n" +
                "      <outgoing>sequenceFlow_76f38862-dbd6-44cb-9e1e-5300475616ad</outgoing>\n" +
                "      <outgoing>sequenceFlow_917a3b23-0f87-4fd3-bc7a-b7a9e4f52c77</outgoing>\n" +
                "    </exclusiveGateway>\n" +
                "    <sequenceFlow id=\"sequenceFlow_b4e12839-3e5a-4d74-a27b-14ef1997aeac\" sourceRef=\"serviceTask_9839a6b6-0000-433f-8dd7-83420c7dc65a\" targetRef=\"exclusiveGateway_31165bb6-71b6-470e-a975-9bf3aee8c4a2\"/>\n" +
                "    <serviceTask id=\"serviceTask_f87ac59f-edce-4974-ad45-646309f69b4a\" name=\"审核人2.1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abd\"/>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_610a845a-9fd2-445d-99a1-9300af328754</incoming>\n" +
                "      <outgoing>sequenceFlow_83b490be-9a41-40c7-9ed5-dfcf8841ed79</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_610a845a-9fd2-445d-99a1-9300af328754\" name=\"条件1\" sourceRef=\"exclusiveGateway_31165bb6-71b6-470e-a975-9bf3aee8c4a2\" targetRef=\"serviceTask_f87ac59f-edce-4974-ad45-646309f69b4a\">\n" +
                "      <conditionExpression id=\"conditionExpression_68348b8e-da07-48ac-8ec0-e5a955f1fab6\">=id&gt;1</conditionExpression>\n" +
                "    </sequenceFlow>\n" +
                "    <serviceTask id=\"serviceTask_29775908-f4d1-42bf-b1b2-a459d8f8e9a2\" name=\"审核人2.1.1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abd.1\"/>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_83b490be-9a41-40c7-9ed5-dfcf8841ed79</incoming>\n" +
                "      <outgoing>sequenceFlow_74fb9bf4-7084-47c0-b8ed-45d4d0698739</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_83b490be-9a41-40c7-9ed5-dfcf8841ed79\" sourceRef=\"serviceTask_f87ac59f-edce-4974-ad45-646309f69b4a\" targetRef=\"serviceTask_29775908-f4d1-42bf-b1b2-a459d8f8e9a2\"/>\n" +
                "    <serviceTask id=\"serviceTask_368a70e7-50f0-45bf-95f4-be92893b1e0b\" name=\"审核人2.1.1.1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abd.1.1\"/>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_74fb9bf4-7084-47c0-b8ed-45d4d0698739</incoming>\n" +
                "      <outgoing>sequenceFlow_d14d9ed8-54fe-4e08-afcd-4c8cb8158701</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_74fb9bf4-7084-47c0-b8ed-45d4d0698739\" sourceRef=\"serviceTask_29775908-f4d1-42bf-b1b2-a459d8f8e9a2\" targetRef=\"serviceTask_368a70e7-50f0-45bf-95f4-be92893b1e0b\"/>\n" +
                "    <serviceTask id=\"serviceTask_0866b168-63dc-4899-b00d-d5d222d689e6\" name=\"审核人2.1.1.1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abd.1.1\"/>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_76f38862-dbd6-44cb-9e1e-5300475616ad</incoming>\n" +
                "      <outgoing>sequenceFlow_6f849fcc-6d01-4cb1-a97c-7a9d9470a46d</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_76f38862-dbd6-44cb-9e1e-5300475616ad\" name=\"条件3\" sourceRef=\"exclusiveGateway_31165bb6-71b6-470e-a975-9bf3aee8c4a2\" targetRef=\"serviceTask_0866b168-63dc-4899-b00d-d5d222d689e6\">\n" +
                "      <conditionExpression id=\"conditionExpression_05e2acb5-a6fa-4e5e-9cdd-4888e94faf3f\">=id&gt;1</conditionExpression>\n" +
                "    </sequenceFlow>\n" +
                "    <exclusiveGateway id=\"exclusiveGateway_14eb5ca4-5c88-439b-b5ec-3952a9936001\" name=\"并行网关\">\n" +
                "      <incoming>sequenceFlow_d14d9ed8-54fe-4e08-afcd-4c8cb8158701</incoming>\n" +
                "      <incoming>sequenceFlow_6f849fcc-6d01-4cb1-a97c-7a9d9470a46d</incoming>\n" +
                "      <incoming>sequenceFlow_917a3b23-0f87-4fd3-bc7a-b7a9e4f52c77</incoming>\n" +
                "      <outgoing>sequenceFlow_6255e0c3-aefc-4406-8c76-809a20da6066</outgoing>\n" +
                "    </exclusiveGateway>\n" +
                "    <sequenceFlow id=\"sequenceFlow_d14d9ed8-54fe-4e08-afcd-4c8cb8158701\" sourceRef=\"serviceTask_368a70e7-50f0-45bf-95f4-be92893b1e0b\" targetRef=\"exclusiveGateway_14eb5ca4-5c88-439b-b5ec-3952a9936001\"/>\n" +
                "    <sequenceFlow id=\"sequenceFlow_6f849fcc-6d01-4cb1-a97c-7a9d9470a46d\" sourceRef=\"serviceTask_0866b168-63dc-4899-b00d-d5d222d689e6\" targetRef=\"exclusiveGateway_14eb5ca4-5c88-439b-b5ec-3952a9936001\"/>\n" +
                "    <sequenceFlow id=\"sequenceFlow_917a3b23-0f87-4fd3-bc7a-b7a9e4f52c77\" name=\"条件2\" sourceRef=\"exclusiveGateway_31165bb6-71b6-470e-a975-9bf3aee8c4a2\" targetRef=\"exclusiveGateway_14eb5ca4-5c88-439b-b5ec-3952a9936001\">\n" +
                "      <conditionExpression id=\"conditionExpression_52a47583-5488-4359-b1d3-1e7273d8188a\">=id&lt;1</conditionExpression>\n" +
                "    </sequenceFlow>\n" +
                "    <serviceTask id=\"serviceTask_7d075115-5e26-4e22-89e9-9e2a0e09eefb\" name=\"审核人3.1\">\n" +
                "      <extensionElements>\n" +
                "        <ns0:taskDefinition type=\"abd.3.1\"/>\n" +
                "      </extensionElements>\n" +
                "      <incoming>sequenceFlow_6255e0c3-aefc-4406-8c76-809a20da6066</incoming>\n" +
                "      <outgoing>sequenceFlow_c9bb65bd-7eae-4ee3-b809-14fbd9568e64</outgoing>\n" +
                "    </serviceTask>\n" +
                "    <sequenceFlow id=\"sequenceFlow_6255e0c3-aefc-4406-8c76-809a20da6066\" sourceRef=\"exclusiveGateway_14eb5ca4-5c88-439b-b5ec-3952a9936001\" targetRef=\"serviceTask_7d075115-5e26-4e22-89e9-9e2a0e09eefb\"/>\n" +
                "    <endEvent id=\"endEvent_fbf0b0ce-29e6-4cc1-8459-421f6a1f23ff\">\n" +
                "      <incoming>sequenceFlow_c9bb65bd-7eae-4ee3-b809-14fbd9568e64</incoming>\n" +
                "    </endEvent>\n" +
                "    <sequenceFlow id=\"sequenceFlow_c9bb65bd-7eae-4ee3-b809-14fbd9568e64\" sourceRef=\"serviceTask_7d075115-5e26-4e22-89e9-9e2a0e09eefb\" targetRef=\"endEvent_fbf0b0ce-29e6-4cc1-8459-421f6a1f23ff\"/>\n" +
                "  </process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_99e43753-ee2b-4af5-acfc-1abdfa1aecff\">\n" +
                "    <bpmndi:BPMNPlane bpmnElement=\"work-flow-id\" id=\"BPMNPlane_f8f6b003-724f-42f6-bb1f-b895cddf805f\">\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"startEvent_999bcc05-d4c1-4607-952a-0c22359201c5\" id=\"BPMNShape_b312a058-638b-4984-9db7-b1c6d98a62cb\">\n" +
                "        <dc:Bounds height=\"36.0\" width=\"36.0\" x=\"100.0\" y=\"100.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_9839a6b6-0000-433f-8dd7-83420c7dc65a\" id=\"BPMNShape_a397b515-e751-4249-a92b-9ff3a98074d6\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"186.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_813b5f65-553a-4087-8829-067bdcd58d6d\" id=\"BPMNEdge_81e0f4e8-b4cb-4154-9cde-e432973d7eac\">\n" +
                "        <di:waypoint x=\"136.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"186.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"exclusiveGateway_31165bb6-71b6-470e-a975-9bf3aee8c4a2\" id=\"BPMNShape_bbb276ea-349c-4f00-afd0-9db76ad786d3\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds height=\"50.0\" width=\"50.0\" x=\"336.0\" y=\"93.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_b4e12839-3e5a-4d74-a27b-14ef1997aeac\" id=\"BPMNEdge_c7b3a331-fb57-4d66-8853-d704356e43ba\">\n" +
                "        <di:waypoint x=\"286.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"336.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_f87ac59f-edce-4974-ad45-646309f69b4a\" id=\"BPMNShape_e706a8a5-54e0-4fac-b8a4-9fc44d636538\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"436.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_610a845a-9fd2-445d-99a1-9300af328754\" id=\"BPMNEdge_9a00e2d4-2fce-4139-b8e9-148884ec9024\">\n" +
                "        <di:waypoint x=\"386.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"436.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_29775908-f4d1-42bf-b1b2-a459d8f8e9a2\" id=\"BPMNShape_9bca4496-6115-4474-8b21-81b954181ea2\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"586.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_83b490be-9a41-40c7-9ed5-dfcf8841ed79\" id=\"BPMNEdge_cdf14e2d-c6e1-41b1-81d1-cf4e93c85b52\">\n" +
                "        <di:waypoint x=\"536.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"586.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_368a70e7-50f0-45bf-95f4-be92893b1e0b\" id=\"BPMNShape_d462ad2e-8c73-448f-879a-c5e6d0a9d6bb\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"736.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_74fb9bf4-7084-47c0-b8ed-45d4d0698739\" id=\"BPMNEdge_f1e67e4e-d381-4f23-ae1c-bbe71b1455d5\">\n" +
                "        <di:waypoint x=\"686.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"736.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_0866b168-63dc-4899-b00d-d5d222d689e6\" id=\"BPMNShape_2e0e6b51-cec2-4710-a6a2-23973f99c8a3\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"436.0\" y=\"208.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_76f38862-dbd6-44cb-9e1e-5300475616ad\" id=\"BPMNEdge_4795125d-1211-4ec7-9ef5-f4631a36173b\">\n" +
                "        <di:waypoint x=\"361.0\" y=\"143.0\"/>\n" +
                "        <di:waypoint x=\"361.0\" y=\"248.0\"/>\n" +
                "        <di:waypoint x=\"436.0\" y=\"248.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"exclusiveGateway_14eb5ca4-5c88-439b-b5ec-3952a9936001\" id=\"BPMNShape_65042911-a173-471e-9b49-962c3fd35b1c\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds height=\"50.0\" width=\"50.0\" x=\"886.0\" y=\"93.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_d14d9ed8-54fe-4e08-afcd-4c8cb8158701\" id=\"BPMNEdge_bb5b2fb2-d182-45b8-a240-df25fbe077e5\">\n" +
                "        <di:waypoint x=\"836.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"886.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_6f849fcc-6d01-4cb1-a97c-7a9d9470a46d\" id=\"BPMNEdge_b1ecbdc7-bed5-477f-a1ba-63948e7b2083\">\n" +
                "        <di:waypoint x=\"536.0\" y=\"248.0\"/>\n" +
                "        <di:waypoint x=\"886.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_917a3b23-0f87-4fd3-bc7a-b7a9e4f52c77\" id=\"BPMNEdge_46c538e5-1ec6-442e-9932-f5a29ffe8c71\">\n" +
                "        <di:waypoint x=\"361.0\" y=\"143.0\"/>\n" +
                "        <di:waypoint x=\"361.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"886.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"serviceTask_7d075115-5e26-4e22-89e9-9e2a0e09eefb\" id=\"BPMNShape_ad73722f-664b-411a-add8-47033c6410f8\">\n" +
                "        <dc:Bounds height=\"80.0\" width=\"100.0\" x=\"986.0\" y=\"78.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_6255e0c3-aefc-4406-8c76-809a20da6066\" id=\"BPMNEdge_65ee6107-3197-4915-a76b-7d3802131b81\">\n" +
                "        <di:waypoint x=\"936.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"986.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape bpmnElement=\"endEvent_fbf0b0ce-29e6-4cc1-8459-421f6a1f23ff\" id=\"BPMNShape_0082abdc-5ac7-4aca-a485-5176af4f2ea2\">\n" +
                "        <dc:Bounds height=\"36.0\" width=\"36.0\" x=\"1136.0\" y=\"100.0\"/>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge bpmnElement=\"sequenceFlow_c9bb65bd-7eae-4ee3-b809-14fbd9568e64\" id=\"BPMNEdge_e210ad7a-5a7f-4c4f-9a9f-420d47544a82\">\n" +
                "        <di:waypoint x=\"1086.0\" y=\"118.0\"/>\n" +
                "        <di:waypoint x=\"1136.0\" y=\"118.0\"/>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</definitions>";
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
