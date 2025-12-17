-- this created tables code if you have postgressql and pgadmin and want to test in you machine
--this is for creating the application tables
CREATE TABLE applications (
    id SERIAL PRIMARY KEY,
    job_id INT NOT NULL REFERENCES jobs(id),
    applicant_id INT NOT NULL REFERENCES users(id),
    resume_url TEXT,
    status VARCHAR(20) DEFAULT 'pending',
    applied_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT unique_application UNIQUE (applicant_id, job_id)
);


--this is for creating the saved_jobs tables
--and here i make already the check of preventing the applicant for adding the same job to the saved again if it is already existed
CREATE TABLE saved_jobs (
    id SERIAL PRIMARY KEY,
    applicant_id INT NOT NULL REFERENCES users(id),
    job_id INT NOT NULL REFERENCES jobs(id),
    saved_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT unique_saved_job UNIQUE (applicant_id, job_id)
);

--this is for creating the applicant tables
-- and the skills is in json format so it can accept value like that ["java script ", "SQL", "node js"]
-- and this is another way to store multivalue in the single column rather than making seperate table for it
CREATE TABLE applicants (
    user_id INT PRIMARY KEY REFERENCES users(id),
    phone VARCHAR(20),
    resume_url TEXT,
    skills JSON,              
    experience_years INT
);

--this is for creating the companies tables
CREATE TABLE company_profiles (
    user_id INT PRIMARY KEY REFERENCES users(id), 
    website TEXT,
    description TEXT,
    industry VARCHAR(100)
);

--this is for creating the users table 
CREATE TABLE users (
    id SERIAL PRIMARY KEY,              
    name VARCHAR(255) NOT NULL,         
    email VARCHAR(255) UNIQUE NOT NULL, 
    password TEXT NOT NULL,      
    role VARCHAR(20) NOT NULL,         
    created_at TIMESTAMP DEFAULT NOW()  
);

--this is for creating the users table
CREATE TABLE jobs (
    id SERIAL PRIMARY KEY, 
    company_id INT NOT NULL REFERENCES users(id), 
    title VARCHAR(255) NOT NULL,                  
    description TEXT,                              
    responsibilities TEXT NOT NULL,
    requirements TEXT,              --skills: css, html, js      --skills - css, html
    career_level VARCHAR(50),                      
    job_type VARCHAR(50),                          
    workplace VARCHAR(50),                         
    country VARCHAR(100),                        
    city VARCHAR(100),                          
    job_category VARCHAR(100),                   
    salary VARCHAR(50),                             
    status VARCHAR(20) DEFAULT 'active',          
    posted_at TIMESTAMP DEFAULT NOW()            
);


