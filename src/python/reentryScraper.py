import requests
from bs4 import BeautifulSoup
import json

class State:
    def __init__(self):
        self.Name = ""
        self.Programs = []

    def __str__(self):
        return Name + str(Programs)

class ReentryProgram:
    def __init__(self, title, link, description):
        self.title = title
        self.link = link
        self.description = description

    def __str__(self):
        return str(self.title) + " ;; " + str(self.link) + " ;; " + str(self.description)

def scrape_page(page_soup):
    reentry_soup = page_soup.find_all('div', class_='fusion-text')
    allStates = []
    currentState = State()
    for soup in reentry_soup:
        if soup.find_all('h3').__len__() == 0:
            continue
        else:
            final_soup = soup


    for soup in final_soup:
        if soup.name == 'h3':
            if currentState.Programs.__len__() == 0:
                print("")
            else:
                allStates.append(currentState)
                
            currentState = State()
            currentState.Name = soup.get_text()
            currentState.Programs = []
            print("#!" + currentState.Name)

        elif soup.name == 'p':
            if soup.find_all('a').__len__() == 0:
                continue
            if soup.find_all('a').__len__() == 2:
                if soup.find_all('a')[1].has_attr('href'):
                    link = soup.find_all('a')[1].attrs['href']
                else:
                    continue
            else:
                if soup.find('a').has_attr('href'):
                    link = soup.find('a').attrs['href']
                else:
                    continue
            rp = ReentryProgram(soup.find('a').get_text(), link, soup.get_text())
            currentState.Programs.append(rp)
            print(rp)
    return allStates


def post_programs(programs):
  for state in programs:
      for program in state.Programs:
          requests.post("http://localhost:9060/api/programs", headers={'content-type': 'application/json'}, data=json.dumps({'programTitle':program.title,'programLink':program.link,'programDesc':program.description}))
          

if __name__ == '__main__':
    programs_list = []
    first_page = requests.get("https://helpforfelons.org/reentry-programs-ex-offenders-state/")
    page_soup = BeautifulSoup(first_page.content, 'html.parser')
    programs_list.extend(scrape_page(page_soup))
    post_programs(programs_list)
