package com.university.narm.explorer;

import java.util.List;

public class Paper
{
    private List<String> references;
    private String id;
    private String title;

    public List<String> getReferences()
    {
        return references;
    }

    public boolean containsKeyWord(String keyword)
    {
        return title.toLowerCase().contains(keyword.toLowerCase());
    }

    public String getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "Paper{" + "id=" + id + ", title=" + title + '}';
    }
}
