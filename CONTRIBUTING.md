# Contributing

As mentioned in the README.md, this was a university coursework submission. I have now made this open to contributions to continue improving the simulation.

Pull requests are welcome but for major changes, please open an issue first to discuss what you would like to change. And, please make sure to update tests as appropriate.

## Maven

This project is using maven, but I am new to it was I'm not sure what to say here... 

## VS Code Dev Container

As an experiment, I am utilising VS Code's [Dev Containers](https://code.visualstudio.com/docs/remote/containers) to provide a consistent environment by hosting the VS Code appication and Java platform within a docker containter. If you wish to use this, then install VS Code and the Remote Development extension. You should be prompted to reopen with the container.

## Structure 

The consists of two Maven projects. 
- `simulation/` is the core, reusable framework. 
- `amazon/` is the specific simulation utilising the above framework.
