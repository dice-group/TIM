# TIM: Tiered Iterative Knowledge Graph Matching

_This is a Java implementation of the paper "TIM: Tiered Iterative Knowledge Graph Matching"_


## Reproducibility
The results of the paper can be verified easily using one of two ways:
1) The accuracy and runtime were computed independently by the Ontology Alignment Evaluation Initiative (OAEI) in the 2025 knowledge graph track.
You can find the results on the OAEI website: https://oaei.ontologymatching.org/2025/results/knowledgegraph/index.html.
For the evaluation, we submitted the docker container contained in this repository "tim-1.0-web-latest.tar.gz".
2) You can clone this repository and open it in an IDE of your choice. Then run the EvaluationPlayground class, which starts the evaluation using the MELT toolkit (https://github.com/dwslab/melt).

## Running TIM For Arbitrary Datasets
You can either use the docker container provided by us within this repository or build it yourself.
This functionality is enabled by the MELT framework.
To build the container yourself, Maven, Java, and Docker must be installed and Docker must be running.
Clone this repository and run "mvn clean install". The final docker container will then be in the "/target" directory.

After starting the container with port 8080 exposed, you can run TIM for two arbitrary knowledge graphs on the URL http://localhost:8080/match/ 
