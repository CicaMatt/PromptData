<div align="center">

[![Python][python-shield]][python-url]
[![Pandas][pandas-shield]][pandas-url]
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![LinkedIn][linkedin-shield]][linkedin-url]


  <h2 align="center">Definition and Evaluation of a Prompt Engineering Template Catalog for the Security and Quality of Code Generated by Large Language Models</h2>

  <p align="center">
    A benchmark study on the impact of directive-based Prompt Engineering techniques on the security and quality of artificially generated code.
  </p>
</div>

## About The Project
This project evaluated the efficiency of various prompt engineering techniques in improving the security and quality of code generated by multiple large language models. Specifically, three different prompt templates of varying complexity were tested across 90 Stack Overflow questions, and the corresponding results were collected.
The models employed for the evaluation are GPT (4o), Gemini (1.5 Pro), Microsoft Copilot (GPT Custom) and CodeLlama (7B). The language evaluated for the experiment are Python, Java and PHP.
<br><br>
Here are some key contribution:
- Dataset Construction: A specific dataset will be built by collecting questions from Stack Overflow that broadly and heterogeneously represent real-world issues.
- Experimentation: Each question will be evaluated in combination with different templates and tested on various types of LLMs to determine whether the effectiveness of the defined prompting strategy is generalizable to the models considered.
- Evaluation: Each of the generated code snippets will be analyzed using Sonarlint, gathering quantitative and qualitative information on the security level and quality of the collected observations, determining the effectiveness of the defined approach.

## Repository Contents
### _data_ 
Contains all the data collected with the analysis of the code snippets produced through LLMs generation.
### _data_analysis_
Contains all the necessary script needed to calculate the metrics used for the experiment evaluation.
### _data_dependencies_ 
Contains all the external dependencies needed for the code analysis for each language employed for the experiment.
For dependency management we used Maven for Java, Conda for Python and Composer for PHP. 
### _generated_code_
Contains all the generated code snippets for each of the question collected by Stack Overflow, available in the _data_ directory.
Each question has its dedicated folder, labeled with the related Stack Overflow question ID, and contains the generated snippets with the models considered.
### _sonarlint_rules_
Contains all the Sonarlint rules used for the analysis of the code snippets in the _generated_code_ folder. The employed rules are splitted by programming language.
### _templates_
Contains all prompt templates used in combination with Stack Overflow question to evaluate the experiment.



## Getting Started

### Prerequisites
Python 3.11.9

### Installation

1. Clone the Repository:
   ```sh
   git clone https://github.com/CicaMatt/PromptData.git
   ```
2. Install Dependencies:
   ```sh
   pip install pandas
   pip install scipy
   ```
   
## Contact Me!
matteocicalese01@gmail.com
<br>
[Linkedin](https://www.linkedin.com/in/cicamatt/)



[python-shield]: https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54
[python-url]: https://www.python.org/
[pandas-shield]: https://img.shields.io/badge/pandas-150458?style=for-the-badge&logo=pandas&logoColor=white
[pandas-url]: https://pandas.pydata.org/
[contributors-shield]: https://img.shields.io/github/contributors/alfcan/drama.svg?style=for-the-badge
[contributors-url]: https://github.com/alfcan/drama/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/alfcan/drama.svg?style=for-the-badge
[forks-url]: https://github.com/alfcan/drama/network/members
[stars-shield]: https://img.shields.io/github/stars/alfcan/drama.svg?style=for-the-badge
[stars-url]: https://github.com/alfcan/drama/stargazers
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/cicamatt/
