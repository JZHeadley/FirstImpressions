import requests
from bs4 import BeautifulSoup

import json



class Job:
    def __init__(self, job_title, job_link, job_desc, company, city):
        self.job_title = job_title
        self.job_link = job_link
        self.job_desc = job_desc
        self.company = company
        self.city = city

    def __str__(self):
        return "%s %s %s %s %s" % (self.job_title, self.job_link, self.job_desc, self.company, self.city)

def scrape_page(page_soup):
    jobs_soup = page_soup.find_all('li', class_='searchjobsc')
    if jobs_soup.__len__() == 0:
        jobs_soup = page_soup.find_all('li', class_='searchjob')
    jobs = []
    for job in jobs_soup:
        header = job.find('a')
        job_title = header.get_text()
        job_link = header.attrs['href']
        fields = job.find_all('p')
        job_desc = fields[0].text
        city = fields[1].text
        company = fields[2].text
        jobs.append(Job(job_title, job_link, job_desc, company, city))

    return jobs


def post_jobs(jobs):
    for job in jobs:
        requests.post("http://localhost:9060/api/jobs", headers={'content-type': 'application/json'}, data=json.dumps({'jobTitle':job.job_title, 'jobLink': job.job_link, 'jobDesc': job.job_desc, 'company': job.company, 'city': job.city}))

if __name__ == '__main__':
    jobs_list = []
    first_page = requests.get("https://www.jobsforfelonshub.com/locations/jobs-for-felons-in-richmond-virginia/")
    page_soup = BeautifulSoup(first_page.content, 'html.parser')

    next_link = page_soup.find('a', {"class": ["button", "button-white", "button-normal"]}).attrs['href']
    while next_link.__len__() > 0:
        page_soup = BeautifulSoup(requests.get(next_link).content, 'html.parser')
        jobs_list.append(scrape_page(page_soup))
        old_link = next_link
        links = page_soup.find_all('a', {"class": ["button", "button-white", "button-normal"]})
        if links.__len__() > 2:
            next_link = links[1].attrs['href']
        if next_link == old_link:
            next_link = ""
    all_jobs = []
    for job_list in jobs_list:
        for job in job_list:
            all_jobs.append(job)

    for job in all_jobs:
        print(job)
    post_jobs(all_jobs)
