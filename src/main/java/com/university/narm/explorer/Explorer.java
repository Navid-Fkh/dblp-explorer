package com.university.narm.explorer;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Explorer
{
    private final String filePath;
    private final String keyword;

    private final Map<String, Paper> papersMap;
    private final List<String> allPapers;
    private Map<String, String> currentTier;

    public Explorer(String filePath, String keyword)
    {
        this.filePath = filePath;
        this.keyword = keyword;
        papersMap = new ConcurrentHashMap<>();
        allPapers = new ArrayList<>();
        currentTier = new ConcurrentHashMap<>();
    }

    private void readPapers() throws IOException
    {
        // Read 5,000 lines at a time
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<String> lines;
            boolean eof = false;
            while (!eof) {
                lines = new ArrayList<>();
                while (lines.size() < 5000) {
                    if ((line = br.readLine()) == null) {
                        eof = true;
                        break;
                    } else
                        lines.add(line);
                }
                i += 5000;
//                if (i == 1000000) // to make testing easier
//                    break;
                System.out.println("Lines readed -> " + i);
                lines.parallelStream().forEach(l -> createPaper(l));
            }
        }
        if (currentTier == null || currentTier.isEmpty())
            System.out.println("No papers found with the keyword: " + keyword);
    }

    private void createPaper(String line)
    {
        Gson gson = new Gson();
        Paper paper = gson.fromJson(line, Paper.class);
        String id = paper.getId();
        if (paper.containsKeyWord(keyword))
            currentTier.put(id, id);
        allPapers.add(id);
        papersMap.put(id, paper);
    }

    private boolean hasReferences(String id)
    {
        List<String> references = papersMap.get(id).getReferences();
        if (references != null)
            return references.parallelStream().anyMatch(ref -> currentTier.containsKey(ref.trim()));
        return false;
    }

    public List<Paper> nextTire() throws IOException
    {
        if (allPapers.isEmpty()) {
            readPapers();
            return getCurrentTireList();
        }

        List<String> tier = allPapers
                .parallelStream()
                .filter(id -> hasReferences(id))
                .collect(Collectors.toList());

        if (tier != null && !tier.isEmpty()) {
            currentTier = tier.parallelStream().collect(Collectors.toConcurrentMap(p -> p, p -> p));
            return getCurrentTireList();
        } else
            return null;

    }

    private List<Paper> getCurrentTireList()
    {
        List<Paper> result = new ArrayList<>();
        currentTier.entrySet().parallelStream().forEach(entry -> result.add(papersMap.get(entry.getKey())));
        return result;
    }

}
