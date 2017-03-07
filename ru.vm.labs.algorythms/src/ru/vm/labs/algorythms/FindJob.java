package ru.vm.labs.algorythms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Job {
	int xCoordinate;
	int yCoordinate;
	
	String description;
	
	public Job (int x, int y, String desc)
	{
		xCoordinate = x;
		yCoordinate = y;
		description = desc;
	}
	
	public int getX()
	{
		return xCoordinate;
	}
	
	public int getY()
	{
		return yCoordinate;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String toString()
	{
		return description + " at (" + xCoordinate + ", " + yCoordinate + ")";
	}
}

public class FindJob {
	
	private List<Job> allJobs;
	
	public FindJob(List<Job> jobs)
	{
		allJobs = jobs;
	}
	
	public List<Job> getJobList(int k)
	{
		System.out.println("unsorted list: ");
		for(Job j: allJobs)
		{
			System.out.println(j);
		}
		
		Collections.sort(allJobs, new Comparator<Job> () {

			@Override
			public int compare(Job o1, Job o2) {
				
				if (getJobDistance(o1) > getJobDistance(o2))
					return 1;
				else if (getJobDistance(o1) == getJobDistance(o2))
					return 0;
				else 
					return -1;
			
			}}) ;
		
		System.out.println("sorted list: ");
		for(Job j: allJobs)
		{
			System.out.println(j);
		}
		
		return allJobs.subList(0, k);
	}
	
	// return a distance between a location of a job and the current point(0,0)
	private int getJobDistance(Job job)
	{
		return (int)Math.sqrt(job.getX()*job.getX() +  job.getY()*job.getY());
	}

	public static void TestMe()
	{
		Job j1 = new Job(10, 3, "engineer 1");
		Job j2 = new Job(10, 5, "engineer 2");
		Job j3 = new Job(4, 6, "engineer 3");
		Job j4 = new Job(6, 1, "engineer 4");
		Job j5 = new Job(7, 7, "engineer 5");
		Job j6 = new Job(7, 2, "engineer 6");
		Job j7 = new Job(11, 8, "engineer 8");
		Job j8 = new Job(2, 5, "engineer 9");
		Job j9 = new Job(2, 2, "engineer 10");
		
		List<Job> jobs = new ArrayList<>();
		
		jobs.add(j1); jobs.add(j2); jobs.add(j3); jobs.add(j4); jobs.add(j5);
		jobs.add(j6); jobs.add(j7); jobs.add(j8);  jobs.add(j9);
		
		FindJob searcher = new FindJob(jobs);
		
		List<Job> nearestJobs = searcher.getJobList(3);
		
		System.out.println("nearest jobs are: ");
		for(Job j: nearestJobs)
		{
			System.out.println(j);
		}
	}
}
