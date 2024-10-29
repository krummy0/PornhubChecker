# PornhubChecker
Checks if PornHub accounts are premium from a file of combos

## Features
 - Multithreaded
 - 100% request based
 - Rotate User Agents
 - Rotate Proxies

## Flags
### Required Flags

- `-f` or `--file`: File with combos

### Optional Flags

- `-d` or `--delimiter`: Character used to separate emails and passwords  
  *(Must be the same in combos to check and in Instagram logs)*  
  *(Default is `|`)*

- `--output-file`: Program will save information to this file in JSON format  
  *(Default is `Output/output.json`)*

- `--output-normal-file`: Program will save information to this file in JSON format  
  *(Will only save premium accounts if this is not set)*

- `--useragents-file`: Program will read a list of user agents from this file  
  *(Default is `Resources/userAgents.json`)*  
  *(Download lists from: [useragents.me](https://www.useragents.me/))*

- `--threads`: How many threads to use  
  *(Default is `5`)*

- `--attempts`: How many times to retry a failed connection with different Proxy and UserAgent  
  *(Default is `5`)*

- `--proxy-file`: File to read proxies from  
  *(Default is `Resources/proxies.txt`)*


## Dependancies
 - Brotli
 - ZSTD
 - JSON Simple

## Coming Soon
 - If you have a premium log I can test with message me on telegram (@krummy01)
 - Detecting if accounts are premium is untested as I do not have a account that is premium. When I confirm program is working I will compile a release
