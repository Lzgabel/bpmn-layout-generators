# BPMN Layout Generator Java Implementation

## Requirements

> JDK 8 or JDK 11

## Build

The project bundles a Maven Wrapper, so just run
``` bash
./mvnw package

编辑 target/generated-sources/jaxb/io/process/analytics/tools/bpmn/generator/internal/generated/model/TDefinitions.java 文件
添加如下属性(用于 Camunda Modeler 标识这个 Camunda Cloud 文件)

@XmlAttribute(name = "xmlns:zeebe", required = true)
@XmlSchemaType(name = "anyURI")
protected String zeebe = "http://camunda.org/schema/zeebe/1.0";
```

## Usage

**Note**: for more options, run with the `--help` option

To generate the layout of an existing BPMN file and save the result as a BPMN file, run
```
java -jar target/bpmn-layout-generator-*-jar-with-dependencies.jar --output=<path_to_output_file> <path_to_input_bpmn_file>
```
If you want to have the resulting layout in an SVG file, pass `--output-type=SVG`   
Notice that `ASCII` and `SVG` ouput types have been developed to get feedback when running tests i.e. to get a quick preview of the
algorithm result. They are not fully implemented and won't probably never be (if you have some interest on that
topic, feel free to provide a Pull Request)


To generate BPMN semantic and diagram layout from discovery CSV files, run
```
java -jar target/bpmn-layout-generator-*-jar-with-dependencies.jar \
  --input-type=CSV \
  --output=<path_to_output_file> \
  csv/PatientsProcess/nodeSimple.csv csv/PatientsProcess/edgeSimple.cs
```
